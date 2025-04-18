package com.dipterv.dipterv.controllerTests

import com.dipterv.dipterv.controller.UserController
import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.dto.FollowDTO
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.model.requestModel.UserUpdateRequest
import com.dipterv.dipterv.security.JwtRequestFilter
import com.dipterv.dipterv.service.FileService
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
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.nio.file.Paths

@WebMvcTest(UserController::class, excludeFilters = [
    ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtRequestFilter::class])
])
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest(@Autowired val mockMvc: MockMvc)  {

    private val objectMapper = ObjectMapper()
    @MockBean
    private lateinit var userService: UserService
    @MockBean
    private lateinit var fileService: FileService

    private val user = UserInfoDTO("1", "name1", "name1","email1", null, null, null, emptyList(), null, null,  emptyList(), emptyList(), emptyList())
    private val users = listOf(user,
        UserInfoDTO("2", "name2", "name2","email2", null, null, null, emptyList(), null, null,  emptyList(), emptyList(), emptyList()),
        UserInfoDTO("3", "name3", "name3","email3", null, null, null, emptyList(), null, null,  emptyList(), emptyList(), emptyList())
           )


    @Test
    fun whenGetAllUser_ReturnTheUserList_WithStatusCode200(){
        val expectedResponse = users
        `when`(userService.getAll()).thenReturn(expectedResponse)

        mockMvc.perform(get("/user/all"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
    }

    @Test
    fun whenSearchByUserName_ReturnCorrectResult_WithStatusCode200(){
        val expectedResponse = listOf(users[0])
        `when`(userService.getAll()).thenReturn(users)
        `when`(userService.nameFilter("name1", users)).thenReturn(expectedResponse)

        mockMvc.perform(get("/user/all?name=name1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
    }

    @Test
    fun whenFindUserById_ReturnsCorrectUserInfo_WithStatusCode200(){
        `when`(userService.findUserInfoDTOById("1")).thenReturn(user)

        mockMvc.perform(get("/user/findById/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(user)))

    }

    @Test
    fun whenUpdateUser_ReturnsUpdatedUser_WithStatusCode200() {
        val updateRequest = UserUpdateRequest("name1", null, null, null, null,)
        val updatedUser = users[0]
        `when`(userService.updateUser("1", updateRequest)).thenReturn(updatedUser)

        mockMvc.perform(put("/user/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)))
    }

    @Test
    fun whenUpdateUser_UserNotFound_Returns404() {
        val updateRequest = UserUpdateRequest("name", "username", "email", null, null)


        `when`(userService.updateUser("1", updateRequest)).thenThrow(NotFoundException("User not found"))

        mockMvc.perform(put("/user/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun whenFollowUser_ReturnsUpdatedUser_WithStatusCode200() {
        val followDto = FollowDTO("1", "2")
        val updatedUser = users[0]
        `when`(userService.followUser(followDto)).thenReturn(updatedUser)

        mockMvc.perform(put("/user/follow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(followDto)))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)))
    }

    @Test
    fun whenUnfollowUser_ReturnsUpdatedUser_WithStatusCode200() {
        val followDto = FollowDTO("1", "2")
        val updatedUser = users[0]
        `when`(userService.unfollowUser(followDto)).thenReturn(updatedUser)

        mockMvc.perform(put("/user/unfollow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(followDto)))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)))
    }

    @Test
    fun whenLikeTravel_ReturnsUser_WithStatusCode200() {
        val updatedUser = users[0]
        `when`(userService.likeTravel("1", "travelId")).thenReturn(updatedUser)

        mockMvc.perform(get("/user/travel/like/1/travelId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)))
    }

    @Test
    fun whenCheckIfTravelLiked_ReturnsTrue_WithStatusCode200() {
        `when`(userService.isTravelLiked("1", "travelId")).thenReturn(true)

        mockMvc.perform(get("/user/travel/liked/1/travelId"))
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun whenUploadProfileImage_ReturnsUpdatedUser_WithStatusCode200() {
        val file = MockMultipartFile("file", "test.jpg", "image/jpeg", byteArrayOf(1, 2, 3))
        `when`(fileService.uploadFile(file, Paths.get("user/profile"),"1")).thenReturn("test.jpg")
        `when`(userService.uploadProfilePicture("1", "test.jpg")).thenReturn(users[0])

        mockMvc.perform(
            multipart("/user/image/upload/1/profile")
                .file(file)
        ).andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(users[0])))
    }

    @Test
    fun whenUploadBackgroundPicture_ReturnsUpdatedUserWithStatus200() {
        val file = MockMultipartFile("file", "background.jpg", "image/jpeg", "background file".toByteArray())
        val fileName = "background.jpg"

        `when`(fileService.uploadFile(file, Paths.get("user/background"), "1")).thenReturn(fileName)
        `when`(userService.uploadBackgroundPicture("1", fileName)).thenReturn(users[0])

        mockMvc.perform(
            multipart("/user/image/upload/1/background")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(users[0])))
    }

    @Test
    fun whenLoadBackgroundPicture_ReturnsImageWithStatus200() {
        val imageBytes = "background file content".toByteArray()
        val resource = ByteArrayResource(imageBytes)

        `when`(fileService.downloadFile(Paths.get("user/background"), "background.jpg")).thenReturn(resource)

        mockMvc.perform(get("/user/image/background/background.jpg"))
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE))
            .andExpect(content().bytes(imageBytes))
    }

    @Test
    fun whenLoadProfilePicture_ReturnsImageWithStatus200() {
        val imageBytes = "profile picture content".toByteArray()
        val resource = ByteArrayResource(imageBytes)

        `when`(fileService.downloadFile(Paths.get("user/profile"), "profile.jpg")).thenReturn(resource)

        mockMvc.perform(get("/user/image/profile/profile.jpg"))
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE))
            .andExpect(content().bytes(imageBytes))
    }
}