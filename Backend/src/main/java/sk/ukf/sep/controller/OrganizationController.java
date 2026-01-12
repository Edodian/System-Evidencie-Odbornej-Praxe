package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.ukf.sep.dto.OrganizationLoginDTO;
import sk.ukf.sep.dto.OrganizationRegistrationDTO;
import sk.ukf.sep.entity.Organization;
import sk.ukf.sep.service.KeycloakOAuthService;
import sk.ukf.sep.service.OrganizationService;

import java.util.Map;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final KeycloakOAuthService keycloakOAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@RequestBody OrganizationRegistrationDTO dto) {
        Organization o = organizationService.registerOrganization(dto);
        return ResponseEntity.ok("Organization registered. Awaiting verification.");
    }

    @GetMapping("/verify/{id}")
    public ResponseEntity<?> verifyOrganization(@PathVariable Integer id) {
        boolean verified = organizationService.verifyOrganization(id);
        return verified
                ? ResponseEntity.ok("Organization verified successfully.")
                : ResponseEntity.badRequest().body("Organization not found.");
    }

    @GetMapping("/unverify/{id}")
    public ResponseEntity<?> unverifyOrganization(@PathVariable Integer id) {
        boolean unverified = organizationService.unverifyOrganization(id);
        return unverified
                ? ResponseEntity.ok("Organization unverified successfully.")
                : ResponseEntity.badRequest().body("Organization not found.");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrganizations() {
        var orgs = organizationService.getAllOrganizations();
        var response = orgs.stream()
                .map(o -> Map.of("id", o.getId(), "name", o.getTitle()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody OrganizationLoginDTO dto) {
        // Keep existing org validation logic intact
        String existing = organizationService.login(dto.getEmail(), dto.getPassword());
        if (existing == null || existing.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password."));
        }

        // Issue OAuth2 token from Keycloak (Direct Grant)
        try {
            Map<String, Object> token;
            try {
                token = keycloakOAuthService.passwordGrant(dto.getEmail(), dto.getPassword());
            } catch (Exception firstTry) {
                keycloakOAuthService.upsertUserWithPasswordAndRole(dto.getEmail(), dto.getPassword(), "COMPANY");
                token = keycloakOAuthService.passwordGrant(dto.getEmail(), dto.getPassword());
            }
            return ResponseEntity.ok(Map.of("token", token.get("access_token")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Authentication service unavailable."));
        }
    }
}
