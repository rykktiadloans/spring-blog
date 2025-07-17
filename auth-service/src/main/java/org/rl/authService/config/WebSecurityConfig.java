package org.rl.authService.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration of Spring Security
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    @Value("${owner.name}")
    private String ownerName;

    @Value("${owner.password}")
    private String ownerPassword;

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    /**
     * Register the JWT Token filter as a bean
     * @return JWT Token filter
     */
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    /**
     * Register the authentication manager as a bean
     * @param authConfig Authentication configuration from which to get the manager
     * @return Authentication manager
     * @throws Exception Can throw an exception if there is an issue
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configure the Spring Security filter chain
     * @param http HTTP security object to configure
     * @return Configured filter chain
     * @throws Exception Can throw an exception if there is an issue
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/v1/auth/validate").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(handling ->
                        handling.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Create a service with user details with an already registered Owner user
     * @return User details service
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername(this.ownerName)
                .password(passwordEncoder().encode(this.ownerPassword))
                .roles("OWNER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Register the standard password encoder
     * @return BCrypt encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
