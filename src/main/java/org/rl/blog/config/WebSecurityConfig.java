package org.rl.blog.config;

import org.rl.blog.exceptions.MissingEnvVariableException;
import org.rl.blog.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private Environment env;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/api/v1/posts", "/api/v1/posts/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts")
                            .hasRole(UserRole.OWNER.toString())
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        String ownerName = env.getProperty("OWNER_NAME");
        String ownerPassword = env.getProperty("OWNER_PASSWORD");
        if(ownerName == null || ownerName.isBlank()) {
            throw new MissingEnvVariableException("Environmental variable 'OWNER_NAME' is missing!");
        }

        if(ownerPassword == null || ownerPassword.isBlank()) {
            throw new MissingEnvVariableException("Environmental variable 'OWNER_PASSWORD' is missing!");
        }

        UserDetails user = User
                .withUsername(ownerName)
                .password(passwordEncoder().encode(ownerPassword))
                .roles(UserRole.OWNER.toString())
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
