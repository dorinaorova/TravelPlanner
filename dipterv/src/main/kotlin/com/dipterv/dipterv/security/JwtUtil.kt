package com.dipterv.dipterv.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtUtil {
    private val SECRET_KEY = Base64.getEncoder().encodeToString("dGhlc2VjcmV0c3RyZW5ndGhlbGVzdGluaXQ==".toByteArray())
    private val EXPIRATION_TIME: Long = 1000 * 60 * 10  //10 min
    private val REFRESH_EXPIRATION_TIME :Long =  7 * 24 * 60 * 60 * 1000 // 7 days

    fun generateToken(username: String): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, username, EXPIRATION_TIME)
    }

    fun generateRefreshToken(username: String): String {
        return createToken(emptyMap(), username, REFRESH_EXPIRATION_TIME)
    }

    private fun createToken(claims: Map<String, Any>, subject: String, expirationTime: Long): String {
        val now = Date(System.currentTimeMillis())
        val expiryDate = Date(now.time + expirationTime)

        val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY))

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String, username: String): Boolean {
        val usernameFromToken = extractUsername(token)
        return (usernameFromToken == username && !isTokenExpired(token))
    }

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractAllClaims(token: String): Claims {
        try {
            val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY))
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            throw RuntimeException("Error decoding JWT: ${e.message}")
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }
}