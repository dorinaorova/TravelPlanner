package com.dipterv.dipterv.serviceTests

import com.dipterv.dipterv.model.DTOMapper
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.FollowDTO
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.model.requestModel.RegisterRequest
import com.dipterv.dipterv.model.requestModel.UserUpdateRequest
import com.dipterv.dipterv.repository.UserRepository
import com.dipterv.dipterv.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class UserServiceTest {

    private val userRepository: UserRepository = mockk()
    private val dtoMapper = DTOMapper()
    private val userService= UserService(userRepository, dtoMapper)
    private val user =User("1", "name1","psw1", "name1","email1", null, null, null, null, null, emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
    private val users = listOf(user,
        User("2", "name2","psw2", "name2","email2", null, null, null, null, null, emptyList(), emptyList(), emptyList(), emptyList(), emptyList()),
        User("3", "name3","psw3", "name3","email3", null, null, null, null, null, emptyList(), emptyList(), emptyList(), emptyList(), emptyList()))

    @Test
    fun whenGetUserById_ReturnsOneUserInfo(){
        every { userRepository.findById("1") } returns Optional.of(user)

        val expectedResult = dtoMapper.userToUserInfoDTO(user)
        val result = userService.findUserInfoDTOById("1")

        assertEquals(expectedResult, result)
    }

    @Test
    fun whenUserRegister_UserDataSavedCorrectly(){
        val savedUser = User(
            null,
            "username",
            "psw",
            "name",
            "email",
            "",
            "",
            "",
            "",
            "",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )

        every { userRepository.save(savedUser) } returns savedUser

        userService.register(RegisterRequest("username", "psw", "email", "name"), "psw")

        verify { userRepository.save(savedUser) }
    }

    @Test
    fun whenUserDataMapped_ReturnsCorrectUserInfo(){
        val expectedResult = UserInfoDTO("1","name1","name1", "email1", null, null, null, emptyList(), null, null, emptyList(), emptyList(), emptyList())
        val result = dtoMapper.userToUserInfoDTO(user)

        assertEquals(expectedResult, result)
    }

    @Test
    fun whenUserUpdatedPartially_OnlyTheModifiedDataWillBeSaved(){
        val updatedUser = user.copy(description = "test")
        every { userRepository.findById("1") } returns  Optional.of(user)
        every { userRepository.save(updatedUser) } returns  updatedUser

        val result = userService.updateUser("1", UserUpdateRequest(null, null, null, null, "test"))

        verify { userRepository.save(updatedUser) }
        assertEquals(dtoMapper.userToUserInfoDTO(updatedUser), result)
    }

    @Test
    fun whenGetAllUser_ReturnsUserInfoList(){
        every { userRepository.findAll() } returns users

        val expectedResult = users.map { dtoMapper.userToUserInfoDTO(it) }

        val result = userService.getAll()

        assertEquals(expectedResult, result)
    }

    @Test
    fun nameFilterTest(){
        val userInfoDTOList = users.map { dtoMapper.userToUserInfoDTO(it) }
        val result = userService.nameFilter("1", userInfoDTOList)

        assertTrue(result.all { it.name.contains("1") })
    }

    @Test
    fun whenFollowUser_AddedTheIdsCorrectly(){
        val follower = user
        val updatedFollower = follower.copy(followingIds = listOf("2"))
        val followed = users[1]
        val updatedFollowed = followed.copy(followerIds = listOf("1"))
        every { userRepository.findById("1") } returns Optional.of(follower)
        every { userRepository.findById("2") } returns Optional.of(followed)
        every {userRepository.save(updatedFollowed)} returns updatedFollowed
        every {userRepository.save(updatedFollower)} returns updatedFollower

        val result = userService.followUser(FollowDTO("1", "2"))

        verify{userRepository.save(updatedFollower)}
        verify{userRepository.save(updatedFollowed)}

        assertEquals(dtoMapper.userToUserInfoDTO(updatedFollowed), result)
    }

    @Test
    fun whenUnfollowUser_RemovedTheIdsCorrectly(){
        val follower = user.copy(followingIds = listOf("2"))
        val updatedFollower = user
        val followed = users[1].copy(followerIds = listOf("1"))
        val updatedFollowed = users[1]
        every { userRepository.findById("1") } returns Optional.of(follower)
        every { userRepository.findById("2") } returns Optional.of(followed)
        every {userRepository.save(updatedFollowed)} returns updatedFollowed
        every {userRepository.save(updatedFollower)} returns updatedFollower

        val result = userService.unfollowUser(FollowDTO("1", "2"))

        verify{userRepository.save(updatedFollower)}
        verify{userRepository.save(updatedFollowed)}

        assertEquals(dtoMapper.userToUserInfoDTO(updatedFollowed), result)
    }

    @Test
    fun whenCreateATravel_TravelIdAdded(){
        val travel = Travel("1","travel",1L, 2L, "country", null, 100,"HUF",null, null, null, null, false, "1")
        val updatedUser=user.copy(travelIds = listOf("1"))
        every { userRepository.save(updatedUser) } returns updatedUser
        every { userRepository.findById("1") } returns Optional.of(user)

        userService.addTravel("1", travel)

        verify { userRepository.save(updatedUser) }
    }

    @Test
    fun whenTravelLiked_AddedTravelIdCorrectly(){
        val travelId="travelId"
        val updatedUser = user.copy(likedTravelIds = listOf(travelId))
        every { userRepository.save(updatedUser) } returns updatedUser
        every { userRepository.findById("1") } returns Optional.of(user)

        val result = userService.likeTravel("1", travelId)

        verify { userRepository.save(updatedUser) }
        assertEquals(dtoMapper.userToUserInfoDTO(updatedUser), result)
    }

    @Test
    fun whenTravelUnliked_RemovedTravelIdCorrectly(){
        val travelId="travelId"
        val originalUser = user.copy(likedTravelIds = listOf(travelId))
        every { userRepository.save(user) } returns user
        every { userRepository.findById("1") } returns Optional.of(originalUser)

        val result = userService.likeTravel("1", travelId)

        verify { userRepository.save(user) }
        assertEquals(dtoMapper.userToUserInfoDTO(user), result)
    }

    @Test
    fun whenProfilePictureUploaded_PropertyUpdatedCorrectly(){
        val profilePictureFilePath = "profilePictureFilePath"
        val updatedUser = user.copy(profilePictureFilePath = profilePictureFilePath)
        every { userRepository.save(updatedUser) } returns updatedUser
        every { userRepository.findById("1") } returns Optional.of(user)

        val result = userService.uploadProfilePicture("1", profilePictureFilePath)

        verify { userRepository.save(updatedUser) }

        assertEquals(dtoMapper.userToUserInfoDTO(updatedUser), result)
    }

    @Test
    fun whenBackgroundPictureUploaded_PropertyUpdatedCorrectly(){
        val backgroundPictureFilePath = "backgroundPictureFilePath"
        val updatedUser = user.copy(backgroundPictureFilePath = backgroundPictureFilePath)
        every { userRepository.save(updatedUser) } returns updatedUser
        every { userRepository.findById("1") } returns Optional.of(user)

        val result = userService.uploadBackgroundPicture("1", backgroundPictureFilePath)

        verify { userRepository.save(updatedUser) }

        assertEquals(dtoMapper.userToUserInfoDTO(updatedUser), result)
    }
}