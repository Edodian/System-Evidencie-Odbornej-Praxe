package sk.ukf.sep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakOAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${keycloak.realm:sep}")
    private String realm;

    @Value("${keycloak.client-id:sep-backend}")
    private String clientId;

    // May be empty if you configure client as Public (not recommended, but supported)
    @Value("${keycloak.client-secret:}")
    private String clientSecret;

    @Value("${keycloak.admin-username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin-password:admin}")
    private String adminPassword;

    private String tokenUrl(String realmName) {
        return baseUrl + "/realms/" + realmName + "/protocol/openid-connect/token";
    }

    public Map<String, Object> passwordGrant(String username, String password) {
        String url = tokenUrl(realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }
        form.add("username", username);
        form.add("password", password);
        form.add("scope", "openid profile email");

        ResponseEntity<Map> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(form, headers),
                Map.class
        );

        @SuppressWarnings("unchecked")
        Map<String, Object> body = resp.getBody();
        return body == null ? Collections.emptyMap() : body;
    }

    /**
     * Ensures the user exists in Keycloak, sets password, and assigns a realm role (if roleName not blank).
     * Used so frontend never talks to Keycloak; backend “operates” Keycloak.
     */
    public void upsertUserWithPasswordAndRole(String emailAsUsername, String password, String roleName) {
        String adminToken = getAdminAccessToken();

        String userId = findUserId(adminToken, emailAsUsername);
        if (userId == null) {
            userId = createUser(adminToken, emailAsUsername);
        }

        if (userId == null) {
            throw new IllegalStateException("Keycloak user upsert failed (cannot resolve user id).");
        }

        resetPassword(adminToken, userId, password);

        // Normalize ROLE_COMPANY -> COMPANY
        String normalizedRole = roleName;
        if (normalizedRole != null && normalizedRole.startsWith("ROLE_")) {
            normalizedRole = normalizedRole.substring("ROLE_".length());
        }

        if (normalizedRole != null && !normalizedRole.isBlank()) {
            assignRealmRole(adminToken, userId, normalizedRole);
        }
    }

    private String getAdminAccessToken() {
        String url = tokenUrl("master"); // admin user lives in master realm

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "admin-cli");
        form.add("username", adminUsername);
        form.add("password", adminPassword);

        ResponseEntity<Map> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(form, headers),
                Map.class
        );

        Object token = resp.getBody() == null ? null : resp.getBody().get("access_token");
        if (token == null) {
            throw new IllegalStateException("Failed to obtain Keycloak admin token.");
        }
        return token.toString();
    }

    private String findUserId(String adminToken, String username) {
        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/admin/realms/" + realm + "/users")
                .queryParam("username", username)
                .queryParam("exact", true)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<List> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        );

        List<?> users = resp.getBody();
        if (users == null || users.isEmpty()) return null;

        Object first = users.get(0);
        if (!(first instanceof Map<?, ?> m)) return null;

        Object id = m.get("id");
        return id == null ? null : id.toString();
    }

    private String createUser(String adminToken, String usernameEmail) {
        String url = baseUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "username", usernameEmail,
                "email", usernameEmail,
                "enabled", true,
                "emailVerified", true
        );

        ResponseEntity<Void> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Void.class
        );

        URI loc = resp.getHeaders().getLocation();
        if (loc != null) {
            String path = loc.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        }

        // Fallback: search again
        return findUserId(adminToken, usernameEmail);
    }

    private void resetPassword(String adminToken, String userId, String password) {
        String url = baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "type", "password",
                "temporary", false,
                "value", password
        );

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                Void.class
        );
    }

    private void assignRealmRole(String adminToken, String userId, String roleName) {
        // GET role representation
        String roleUrl = baseUrl + "/admin/realms/" + realm + "/roles/" + roleName;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<Map> roleResp = restTemplate.exchange(
                roleUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        Map role = roleResp.getBody();
        if (role == null) return;

        // POST to role-mappings/realm
        String mapUrl = baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(
                mapUrl,
                HttpMethod.POST,
                new HttpEntity<>(List.of(role), headers),
                Void.class
        );
    }
}
