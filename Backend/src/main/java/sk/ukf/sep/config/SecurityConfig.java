package sk.ukf.sep;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // отключаем CSRF, если фронт на другом домене
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll() // публичные пути
                        .anyRequest().authenticated() // остальные требуют логина
                )
                .formLogin(Customizer.withDefaults()) // стандартный login form Spring Security
                .logout(Customizer.withDefaults());  // стандартный logout

        return http.build();
    }
}
