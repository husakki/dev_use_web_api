package org.tzi.use.api_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import org.tzi.use.api_security.exceptions.CustomAuthenticationEntryPoint;
import org.tzi.use.api_security.rest.middleware.AuditLogMiddleware;
import org.tzi.use.api_security.rest.middleware.AuthenticationMiddleware;
import org.tzi.use.api_security.rest.middleware.ExceptionHandlerMiddleware;
import org.tzi.use.api_security.rest.middleware.RateLimitMiddleware;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private ExceptionHandlerMiddleware exceptionHandlerMiddleware;

	@Autowired
	private RateLimitMiddleware rateLimitMiddleware;

	@Autowired
	private AuthenticationMiddleware authenticationMiddleware;

	@Autowired
	private AuditLogMiddleware auditLogMiddleware;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		// Since CustomAuthProvider is annotated with @Component, spring detects it as
		// Bean and use it for dependency injection in the automatically created default
		// AuthenticationManager because it is implementing the AuthenticationProvider
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // disable crsf because we are stateless (no cookies for authentication)
			.authorizeHttpRequests((request) -> request
					// .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
					// .requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
					.requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
					.requestMatchers(HttpMethod.PUT, "/auth/login").permitAll()
					.requestMatchers("/error").permitAll()
					.anyRequest().authenticated()) // Require all request except the above to be authenticated
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(exceptionHandlerMiddleware, DisableEncodeUrlFilter.class) // The very first filter
			.addFilterAfter(rateLimitMiddleware, ExceptionHandlerMiddleware.class)
			.addFilterAfter(auditLogMiddleware, SecurityContextHolderFilter.class)
			.addFilterAfter(authenticationMiddleware, AuditLogMiddleware.class)
			.exceptionHandling((exception) -> exception
					.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

		// Need to set the AuditLog before AuthenticationMiddleware (and after
		// SecurityContextHolderFilter to access it), because we also
		// want to log errors happening in the AuthenticationMiddleware which would not
		// be possible if being executed after it. We can still get the user if it
		// exists after the request was processed (so getting it on the way out and not
		// in)

		return http.build();
	}
}