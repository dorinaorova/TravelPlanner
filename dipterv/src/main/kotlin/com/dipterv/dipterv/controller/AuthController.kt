package com.dipterv.dipterv.controller

import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.LoginDTO
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.model.requestModel.LoginRequest
import com.dipterv.dipterv.model.requestModel.RefreshTokenRequest
import com.dipterv.dipterv.model.requestModel.RegisterRequest
import com.dipterv.dipterv.repository.UserRepository
import com.dipterv.dipterv.security.JwtUtil
import com.dipterv.dipterv.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import kotlin.math.log

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
){
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.userName, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val user = userService.findByUsername(loginRequest.userName)
        if(user != null){
            val jwt = jwtUtil.generateToken(loginRequest.userName)
            val refreshToken = jwtUtil.generateRefreshToken(loginRequest.userName)
            return ResponseEntity.ok(LoginDTO(user._id!!, jwt, refreshToken))
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password")
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<Any> {
        val refreshToken = request.refreshToken
        val username = jwtUtil.extractUsername(refreshToken)
        println(jwtUtil.validateToken(refreshToken, username))
        if (username != null && jwtUtil.validateToken(refreshToken, username)) {
            val userDetails = userService.loadUserByUsername(username)
            val newAccessToken = jwtUtil.generateToken(userDetails.username)
            val newRefreshToken = jwtUtil.generateRefreshToken(userDetails.username)
            return ResponseEntity.ok(LoginDTO("", newAccessToken, newRefreshToken))
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token")
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): UserInfoDTO {
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        return userService.register(registerRequest, encodedPassword)
    }

    @PostMapping("/refresh-token/check")
    fun checkRefreshToken(@RequestBody request: RefreshTokenRequest) : Boolean{
        try{

        val token = request.refreshToken
        val isRefreshTokenExpired = jwtUtil.validateToken(token, jwtUtil.extractUsername(token))
        return  isRefreshTokenExpired
        }catch(e:Exception){
            return false
        }

    }
}