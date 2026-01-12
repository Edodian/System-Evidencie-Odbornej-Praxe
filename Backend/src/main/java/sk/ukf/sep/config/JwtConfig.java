// src/main/java/sk/ukf/sep/config/JwtConfig.java
package sk.ukf.sep.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Provided via docker-compose env var:
    // SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
    private String jwkSetUri;

    // === ENCODER (kept to avoid breaking any existing code paths that still issue HS256 tokens) ===
    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(key);
        return new NimbusJwtEncoder(jwkSource);
    }

    // === DECODER (Keycloak RS256 via JWKS if configured; otherwise HS256 local) ===
    @Bean
    public JwtDecoder jwtDecoder() {
        // Local HS256 decoder (existing behavior)
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JwtDecoder hs256Decoder = NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        if (jwkSetUri == null || jwkSetUri.isBlank()) {
            return hs256Decoder;
        }

        JwtDecoder keycloakDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Try Keycloak first; fallback to HS256 to keep old tokens working
        return token -> {
            try {
                return keycloakDecoder.decode(token);
            } catch (JwtException ex) {
                return hs256Decoder.decode(token);
            }
        };
    }
}
