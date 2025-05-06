package com.dipterv.dipterv.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration


@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtRequestFilter: JwtRequestFilter){

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { cors ->
                cors.configurationSource { request ->
                    CorsConfiguration().apply {
                        allowedOrigins = listOf("*")
                        allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        allowedHeaders = listOf("*")
                        allowCredentials = false
                    }
                }
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                    ).permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/user/image/**", "travel/image/**").permitAll()
                    .anyRequest().authenticated()
//                    .anyRequest().permitAll()
            }
            .anonymous { it.disable() }
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }
}