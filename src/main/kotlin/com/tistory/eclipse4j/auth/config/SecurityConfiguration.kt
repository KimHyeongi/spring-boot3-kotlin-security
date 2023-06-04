package com.tistory.eclipse4j.auth.config

import com.tistory.eclipse4j.auth.token.service.LogoutService
import com.tistory.eclipse4j.auth.user.entity.Permission
import com.tistory.eclipse4j.auth.user.entity.Role
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val logoutService: LogoutService
) {


    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers(
                "/api/v1/auth/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
            )
            .permitAll()
            .requestMatchers("/api/v1/management/**").hasAnyRole(Role.ADMIN.name, Role.MANAGER.name)
            .requestMatchers(HttpMethod.GET, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_READ.name, Permission.MANAGER_READ.name)
            .requestMatchers(HttpMethod.POST, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_CREATE.name, Permission.MANAGER_CREATE.name)
            .requestMatchers(HttpMethod.PUT, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name, Permission.MANAGER_UPDATE.name)
            .requestMatchers(HttpMethod.DELETE, "/api/v1/management/**")
            .hasAnyAuthority(Permission.ADMIN_DELETE.name, Permission.MANAGER_DELETE.name)
            /*
            .requestMatchers("/api/v1/admin/ **").hasRole(ADMIN.name())
            .requestMatchers(GET, "/api/v1/admin/ **").hasAuthority(ADMIN_READ.name())
            .requestMatchers(POST, "/api/v1/admin/ **").hasAuthority(ADMIN_CREATE.name())
            .requestMatchers(PUT, "/api/v1/admin/ **").hasAuthority(ADMIN_UPDATE.name())
            .requestMatchers(DELETE, "/api/v1/admin/ **").hasAuthority(ADMIN_DELETE.name())
            */
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout()
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutService)
            .logoutSuccessHandler(LogoutSuccessHandler { request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication? -> SecurityContextHolder.clearContext() })
        return http.build()
    }
}