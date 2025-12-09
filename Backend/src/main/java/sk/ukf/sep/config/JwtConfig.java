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
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // === ENCODER (used when issuing tokens) ===
    @Bean
    public JwtEncoder jwtEncoder() {
        // HS256 shared secret
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        // NOTE: encoder uses constructor with a JWKSource, NOT withSecretKey()
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(key);

        return new NimbusJwtEncoder(jwkSource);
    }

    // === DECODER (used by resource-server filter to validate tokens) ===
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        // Here you *do* use withSecretKey() – it exists on the decoder
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256) // match the algorithm we’ll use when encoding
                .build();
    }
}
