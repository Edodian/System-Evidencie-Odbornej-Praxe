<<<<<<< HEAD
//package sk.ukf.sep.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration COMMENTED OUT with spring-boot-starter-security to run non-prod db with no issues for now
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .formLogin(form -> form.disable())
//                .httpBasic(basic -> basic.disable())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(Customizer.withDefaults());
//
//        return http.build();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // or PasswordEncoderFactories.createDelegatingPasswordEncoder()
//    }
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable()) // отключаем CSRF для REST API
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/login", "/student").permitAll() // логин и регистрация разрешены без авторизации
////                        .anyRequest().authenticated() // остальное требует логина
////                )
////                .formLogin(Customizer.withDefaults()) // стандартная обработка логина
////                .logout(Customizer.withDefaults()) // стандартный logout
////                .cors(Customizer.withDefaults()); // включаем CORS
////
////        return http.build();
////    }
//}
=======
// src/main/java/sk/ukf/sep/config/SecurityConfig.java
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

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // public endpoints (no token)
                        .requestMatchers(HttpMethod.POST,
                                "/api/student/register",
                                "/api/student/login",
                                "/api/student/reset-password",
                                "/api/student/create-password",
                                "/api/student/verify-temp-password"
                        ).permitAll()

                        // everything else requires Bearer token
                        .anyRequest().authenticated()
                )
                // enable OAuth2 Resource Server using JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
>>>>>>> feature/tokenization
