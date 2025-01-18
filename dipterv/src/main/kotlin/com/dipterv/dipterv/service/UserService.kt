package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.DTOMapper
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.*
import com.dipterv.dipterv.model.requestModel.LoginRequest
import com.dipterv.dipterv.model.requestModel.RegisterRequest
import com.dipterv.dipterv.repository.TravelRepository
import com.dipterv.dipterv.repository.UserRepository
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties.Registration
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
class UserService(val userRepository: UserRepository, val mapper: DTOMapper) : UserDetailsService {

    override fun loadUserByUsername(userName: String): UserDetails {
        val user = userRepository.findByUsername(userName) ?: throw NotFoundException("User not found with name: $userName")
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            emptyList()
        )
    }

    fun register(registration: RegisterRequest, password: String): UserInfoDTO{
        val newUser = User(
            null,
            registration.userName,
            password,
            registration.name,
            registration.email,
            "",
            "",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
        return mapper.userToUserInfoDTO(userRepository.save(newUser))
    }

    fun login(loginRequest: LoginRequest, token: String): LoginDTO{
        val user = findByUsername(loginRequest.userName)
        return LoginDTO(user._id!!,token )
    }

    fun getAll(): List<UserInfoDTO>{
        return userRepository.findAll().map{
            user-> mapper.userToUserInfoDTO(user)
        }
    }

    fun findUserDTOById(id: String): UserDTO{
        return mapper.userToUserDTO(findById(id))
    }

    fun findUserInfoDTOById(id: String) : UserInfoDTO{
        return mapper.userToUserInfoDTO(findById(id))
    }

    private fun findById(id: String) : User{
        try {
            return userRepository.findById(id).get()
        }catch (e: Exception){
            throw NotFoundException("User not found with id: $id")
        }
    }

    private fun findByUsername(username: String) : User{
        return userRepository.findByUsername(username)!!
    }

    fun nameFilter(name: String, users: List<UserInfoDTO>) : List<UserInfoDTO>{
        return users.filter{it.name.contains(name, true) }
    }

    fun updateUser(id:String, user : User) : UserInfoDTO{
        try{
            val findUser = findById(id)
            findUser.name= user.name
            findUser.email = user.email
            findUser.description = user.description
            val savedUser = userRepository.save(findUser)
            return mapper.userToUserInfoDTO(savedUser)
        }catch (e: Exception){
            throw NotFoundException("User not found with id: $id")
        }
    }

    fun followUser(follow: FollowDTO) : UserDTO{
            var followerUser = findById(follow.followerId)
            var followedUser = findById(follow.followedId)
            val updatedFollowingList = followerUser.followingIds.toMutableList().apply { add(followedUser._id!!) }
            followerUser = followedUser.copy(followingIds = updatedFollowingList)

            val updatedFollowersList = followedUser.followerIds.toMutableList().apply { add(followerUser._id!!) }
            followedUser = followedUser.copy(followerIds = updatedFollowersList)

            val updated = userRepository.save(followerUser)
            userRepository.save(followedUser)

            return mapper.userToUserDTO(updated)
    }

    fun unfollowUser(follow: FollowDTO) : UserDTO{
        var followerUser = findById(follow.followerId)
        var unfollowedUser = findById(follow.followedId)
        val updateFollowingList = followerUser.followingIds.toMutableList().apply { remove(followerUser._id!!) }
        followerUser = followerUser.copy(followingIds = updateFollowingList)

        val updatedFollowersList = unfollowedUser.followerIds.toMutableList().apply { remove(followerUser._id!!) }
        unfollowedUser = unfollowedUser.copy(followerIds = updatedFollowersList)
        val updated = userRepository.save(followerUser)
        userRepository.save(unfollowedUser)
        return UserDTO(
            updated._id,
            mapper.userToUserInfoDTO(updated),
            updated.followingIds,
            updated.followerIds)
    }

    fun add(user: UserInfoDTO): User? {
        return null
//        val newUser = User(
//            null,
//            user.username,
//            user.name,
//            user.email,
//            user.description,
//            user.profilePictureFilePath,
//            emptyList(),
//            emptyList(),
//            emptyList(),
//            emptyList()
//        )
//        return userRepository.save(newUser)
    }

    fun addTravel(id: String, travel: Travel){
        var user = findById(id)
        val updatedTravelList = user.travelIds.toMutableList().apply { add(travel._id!!) }
        user = user.copy(travelIds = updatedTravelList)
        userRepository.save(user)
    }
}