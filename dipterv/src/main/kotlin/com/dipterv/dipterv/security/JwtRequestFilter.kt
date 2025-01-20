package com.dipterv.dipterv.security

import com.dipterv.dipterv.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtRequestFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserService
) : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try{

        val authorizationHeader = request.getHeader("Authorization")

        var username: String? = null
        var jwt: String? = null
        val uri = request.requestURI

        if (uri.startsWith("/auth/")) {
            filterChain.doFilter(request, response)
            return
        }

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7)
                try {
                    username = jwtUtil.extractUsername(jwt)
                } catch (e: ExpiredJwtException) {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("Token is expired")
                    return
                }
            }

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                if (!jwtUtil.validateToken(jwt!!, userDetails.username)) {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("Invalid token")
                    return
                }

                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }

        filterChain.doFilter(request, response)
        }catch (e: ExpiredJwtException){
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Token is expired")
        }
        catch(e: Exception){
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.writer.write(e.message ?: "Invalid token")
        }
    }
}