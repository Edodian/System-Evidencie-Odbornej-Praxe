package sk.ukf.sep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthConverterConfig().jwtAuthenticationConverter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // =====================
                // BASIC CONFIG
                // =====================
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // =====================
                // AUTHORIZATION RULES
                // =====================
                .authorizeHttpRequests(auth -> auth

                        // ---------- PUBLIC (NO TOKEN) ----------
                        .requestMatchers(HttpMethod.POST,
                                "/api/student/register",
                                "/api/student/login",
                                "/api/student/reset-password",
                                "/api/student/create-password",
                                "/api/student/verify-temp-password",

                                "/api/organization/register",
                                "/api/organization/login"
                        ).permitAll()

                        // ---------- COMPANY (ORGANIZATION) ----------
                        .requestMatchers("/api/organization/**")
                        .hasRole("COMPANY")

                        // ---------- EVERYTHING ELSE ----------
                        .anyRequest().authenticated()
                )

                // =====================
                // JWT RESOURCE SERVER
                // =====================
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
