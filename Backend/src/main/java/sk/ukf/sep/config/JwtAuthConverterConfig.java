package sk.ukf.sep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.*;

@Configuration
public class JwtAuthConverterConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<String> roles = new LinkedHashSet<>();

            // Keycloak realm roles: realm_access.roles
            Object realmAccessObj = jwt.getClaim("realm_access");
            if (realmAccessObj instanceof Map<?, ?> realmAccess) {
                Object rolesObj = realmAccess.get("roles");
                if (rolesObj instanceof List<?> list) {
                    for (Object r : list) {
                        if (r != null) roles.add(r.toString());
                    }
                }
            }

            // Backward compatibility: your existing tokens sometimes used "roles" or "role"
            List<String> rolesClaim = jwt.getClaimAsStringList("roles");
            if (rolesClaim != null) roles.addAll(rolesClaim);

            String roleSingle = jwt.getClaimAsString("role");
            if (roleSingle != null && !roleSingle.isBlank()) roles.add(roleSingle);

            return roles.stream()
                    .filter(r -> r != null && !r.isBlank())
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(r -> (GrantedAuthority) new SimpleGrantedAuthority(r))
                    .toList();
        });

        return converter;
    }
}
