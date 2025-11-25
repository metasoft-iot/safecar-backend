package com.safecar.platform.iam.infrastructure.authorization.sfs.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.safecar.platform.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import com.safecar.platform.iam.infrastructure.authorization.sfs.services.ExtendedUserDetailsService;
import com.safecar.platform.iam.infrastructure.hashing.bcrypt.BCryptHashingService;
import com.safecar.platform.iam.infrastructure.tokens.jwt.BearerTokenService;

import java.util.List;

/**
 * Web Security Configuration
 * <p>
 * Main configuration class for web security in the application.
 * Sets up security filters, authentication, authorization, CORS, CSRF
 * protection,
 * exception handling, session management, and defines which endpoints are
 * publicly accessible.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final BearerTokenService tokenService;
    private final BCryptHashingService hashingService;
    private final ExtendedUserDetailsService userDetailsService;
    private final AuthenticationEntryPoint unauthorizedRequestHandlerEntryPoint;

    public WebSecurityConfiguration(
            @Qualifier("defaultUserDetailsService") ExtendedUserDetailsService userDetailsService,
            BearerTokenService tokenService,
            BCryptHashingService hashingService,
            AuthenticationEntryPoint unauthorizedRequestHandlerEntryPoint) {

        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.hashingService = hashingService;
        this.unauthorizedRequestHandlerEntryPoint = unauthorizedRequestHandlerEntryPoint;
    }

    /**
     * Creates the BearerAuthorizationRequestFilter bean.
     *
     * @return BearerAuthorizationRequestFilter for JWT Bearer token authentication.
     */
    @Bean
    public BearerAuthorizationRequestFilter authorizationRequestFilter(
            BearerTokenService tokenService,
            @Qualifier("defaultUserDetailsService") ExtendedUserDetailsService uds) {
        return new BearerAuthorizationRequestFilter(tokenService, uds);
    }

    /**
     * Provides the AuthenticationManager bean.
     *
     * @param authenticationConfiguration Spring authentication configuration.
     * @return AuthenticationManager instance.
     * @throws Exception if the manager cannot be created.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides the DaoAuthenticationProvider bean.
     *
     * @return DaoAuthenticationProvider configured with user details and password
     *         encoder.
     */
    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(hashingService);
        return provider;
    }

    /**
     * Provides the PasswordEncoder bean.
     *
     * @return PasswordEncoder for encoding user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return hashingService;
    }

    /**
     * Configures the security filter chain.
     * <p>
     * Sets up CORS, disables CSRF, configures exception handling, stateless session
     * management,
     * allows unauthenticated access to specific endpoints, and adds the JWT Bearer
     * filter.
     *
     * @param http HttpSecurity object for configuration.
     * @return Configured SecurityFilterChain.
     * @throws Exception if configuration fails.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // URLs we leave unsecured (including Swagger/OpenAPI and Profiles for testing)
        String[] publicMatchers = {
                "/api/v1/authentication/**",
                "/api/v1/profiles/**",
                "/api/v1/person-profiles/**", // Allow public access for profile creation during sign-up
                "/api/v1/business-profiles/**", // Allow public access for business profile creation
                "/api/v1/drivers/**", // Allow public access to driver data
                "/api/v1/workshops/**", // Allow public access to view workshops
                "/api/v1/vehicles/**", // Allow public access to vehicle data
                "/api/v1/appointments/**", // Allow public access to appointments
                "/api/v1/telemetry/**", // Allow public access to telemetry data
                "/api/v1/mechanics/**", // Allow public access to mechanics (for testing)
                "/api/v1/insights/**", // Allow public access to insights
                "/error", // Allow access to error page
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/webjars/**"
        };

        http
                // CORS
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("*"));
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    return corsConfig;
                }))
                // Disable CSRF as we work with JWT
                .csrf(csrf -> csrf.disable())
                // Exception handling
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedRequestHandlerEntryPoint))
                // Stateless (no HTTP session)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicMatchers).permitAll()
                        .anyRequest().authenticated())
                // Set the authentication provider by calling the bean method.
                .authenticationProvider(authenticationProvider())
                // Insert the JWT Bearer filter before Spring's login filter.
                .addFilterBefore(
                        authorizationRequestFilter(tokenService, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}