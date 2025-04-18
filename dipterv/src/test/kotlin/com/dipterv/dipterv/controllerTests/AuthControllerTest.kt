package com.dipterv.dipterv.controllerTests

import com.dipterv.dipterv.controller.AuthController
import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.model.requestModel.RefreshTokenRequest
import com.dipterv.dipterv.model.requestModel.RegisterRequest
import com.dipterv.dipterv.security.JwtRequestFilter
import com.dipterv.dipterv.security.JwtUtil
import com.dipterv.dipterv.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    AuthController::class, excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtRequestFilter::class])
    ])
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest(@Autowired val mockMvc: MockMvc)  {

    @MockBean
    private lateinit var authenticationManager: AuthenticationManager

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @MockBean
    private lateinit var jwtUtil: JwtUtil

    private val objectMapper = ObjectMapper()

    private val refreshTokenRequest = RefreshTokenRequest("refresh")
    private val registerRequest = RegisterRequest("username", "password", "email", "name")
    private val userInfoDTO =  UserInfoDTO("1", "name1", "name1","email1", null, null, null, emptyList(), null, null,  emptyList(), emptyList(), emptyList())
    private val user = User("1", "name1","psw1", "name1","email1", null, null, null, null, null, emptyList(), emptyList(), emptyList(), emptyList(), emptyList())


    @Test
    fun whenRefreshToken_Invalid_ReturnsUnauthorized() {
        `when`(jwtUtil.extractUsername(refreshTokenRequest.refreshToken)).thenReturn(null)
        `when`(jwtUtil.validateToken(refreshTokenRequest.refreshToken, "")).thenReturn(false)

        mockMvc.perform(post("/auth/refresh-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(refreshTokenRequest)))
            .andExpect(status().isUnauthorized)
            .andExpect(content().string("Invalid refresh token"))
    }

    @Test
    fun whenRegister_Valid_ReturnsUserInfoDTO() {
        `when`(passwordEncoder.encode(registerRequest.password)).thenReturn("encoded-password")
        `when`(userService.register(registerRequest, "encoded-password")).thenReturn(userInfoDTO)

        mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(userInfoDTO)))
    }

    @Test
    fun whenCheckRefreshToken_Valid_ReturnsTrue() {
        `when`(jwtUtil.validateToken(refreshTokenRequest.refreshToken, jwtUtil.extractUsername(refreshTokenRequest.refreshToken)))
            .thenReturn(true)

        mockMvc.perform(post("/auth/refresh-token/check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(refreshTokenRequest)))
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun whenCheckRefreshToken_Invalid_ReturnsFalse() {
        `when`(jwtUtil.validateToken(refreshTokenRequest.refreshToken, jwtUtil.extractUsername(refreshTokenRequest.refreshToken)))
            .thenReturn(false)

        mockMvc.perform(post("/auth/refresh-token/check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(refreshTokenRequest)))
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
    }
}