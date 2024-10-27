package com.dipterv.dipterv.controller

import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.LoginDTO
import com.dipterv.dipterv.model.dto.UserDTO
import com.dipterv.dipterv.model.requestModel.LoginRequest
import com.dipterv.dipterv.model.requestModel.RegisterRequest
import com.dipterv.dipterv.repository.UserRepository
import com.dipterv.dipterv.security.JwtUtil
import com.dipterv.dipterv.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
){
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): LoginDTO {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.userName, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        return userService.login(loginRequest, jwtUtil.generateToken(loginRequest.userName))
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): User {
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        return userService.register(registerRequest, encodedPassword)
    }
}