package br.com.ceasa.security.ad.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Value("${ceasa.security.ad.groups.rolePrefix:ROLE_}")
    private String rolePrefix;

    @Value("${ceasa.security.ad.groups.allowedRoles:USER}")
    private String allowedRolesCsv;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        String[] allowedAuthorities = Arrays.stream(allowedRolesCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toUpperCase)
                .map(r -> rolePrefix + r)
                .toArray(String[]::new);

        http
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/sem-permissao", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().hasAnyAuthority(allowedAuthorities)
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/sem-permissao"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
