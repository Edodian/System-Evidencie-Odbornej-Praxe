package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.ukf.sep.dto.OrganizationRegistrationDTO;
import sk.ukf.sep.service.OrganizationService;


@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody OrganizationRegistrationDTO dto) {
        organizationService.registerOrganization(dto);
        return ResponseEntity.ok("Organization registered. Awaiting verification.");
    }

    @GetMapping("/verify/{id}")
    public ResponseEntity<?> verify(@PathVariable Long id) {
        boolean verified = organizationService.verifyOrganization(id);
        return verified
                ? ResponseEntity.ok("Organization verified successfully.")
                : ResponseEntity.badRequest().body("Organization not found.");
    }
    @GetMapping("/unverify/{id}")
    public ResponseEntity<?> Unverify(@PathVariable Long id) {
        boolean unverified = organizationService.unverifyOrganization(id);
        return unverified
                ? ResponseEntity.ok("Organization unverified successfully.")
                : ResponseEntity.badRequest().body("Organization not found.");
    }
}
