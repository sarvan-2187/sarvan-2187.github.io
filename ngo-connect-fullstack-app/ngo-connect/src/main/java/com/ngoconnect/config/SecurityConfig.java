package com.ngoconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/", "/home", "/about", "/contact").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/ngos", "/ngos/**").permitAll()
                .requestMatchers("/campaigns", "/campaigns/**").permitAll()
                .requestMatchers("/events", "/events/**").permitAll()
                .requestMatchers("/transparency/**").permitAll()
                
                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // API endpoints
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/ngos/**").hasAnyRole("NGO", "ADMIN")
                .requestMatchers("/api/campaigns/**").hasAnyRole("NGO", "ADMIN")
                .requestMatchers("/api/donations/**").hasAnyRole("DONOR", "NGO", "ADMIN")
                .requestMatchers("/api/events/**").hasAnyRole("NGO", "VOLUNTEER", "ADMIN")
                .requestMatchers("/api/volunteer/**").hasAnyRole("VOLUNTEER", "NGO", "ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Dashboard endpoints
                .requestMatchers("/dashboard/donor/**").hasRole("DONOR")
                .requestMatchers("/dashboard/volunteer/**").hasRole("VOLUNTEER")
                .requestMatchers("/dashboard/ngo/**").hasRole("NGO")
                .requestMatchers("/dashboard/admin/**").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions().disable()) // For H2 console
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}

