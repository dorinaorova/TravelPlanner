package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.FollowDTO
import com.dipterv.dipterv.model.dto.TravelDTO
import com.dipterv.dipterv.model.dto.UserDTO
import com.dipterv.dipterv.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val travelService: TravelService) {


    fun getAll(): List<UserInfoDTO>{
        return userRepository.findAll().map{
            user-> userToUserInfoDTOConverter(user)
        }
    }

    fun findUserInfoDTOById(id: String) : UserInfoDTO{
        return userToUserInfoDTOConverter(findById(id))
    }

    private fun findById(id: String) : User{
        try {
            return userRepository.findById(id).get()
        }catch (e: Exception){
            throw NotFoundException("User not found with id: $id")
        }
    }

    fun nameFilter(name: String, users: List<UserInfoDTO>) : List<UserInfoDTO>{
        return users.filter{it.name.contains(name, true) }
    }

    fun updateUser(id:String, user : User) : UserInfoDTO{
        try{
            val findUser = findById(id)
            findUser.username = user.username
            findUser.username = user.username
            findUser.email = user.email
            findUser.description = user.description
            val savedUser = userRepository.save(findUser)
            return UserInfoDTO(savedUser._id, savedUser.username, savedUser.name, savedUser.email, savedUser.description, savedUser.profilePictureFilePath, savedUser.travels)
        }catch (e: Exception){
            throw NotFoundException("User not found with id: $id")
        }
    }

    fun followUser(follow: FollowDTO) : UserDTO{
            var followerUser = findById(follow.followerId)
            var followedUser = findById(follow.followedId)
            val updatedFollowingList = followerUser.following.toMutableList().apply { add(followedUser) }
            followerUser = followedUser.copy(following = updatedFollowingList)

            val updatedFollowersList = followedUser.followers.toMutableList().apply { add(followerUser) }
            followedUser = followedUser.copy(followers = updatedFollowersList)

            val updated = userRepository.save(followerUser)
            userRepository.save(followedUser)

            return UserDTO(
                updated._id,
                userToUserInfoDTOConverter(updated),
                updated.following,
                updated.followers)
    }

    fun unfollowUser(follow: FollowDTO) : UserDTO{
        var followerUser = findById(follow.followerId)
        var unfollowedUser = findById(follow.followedId)
        val updateFollowingList = followerUser.following.toMutableList().apply { remove(followerUser) }
        followerUser = followerUser.copy(following = updateFollowingList)

        val updatedFollowersList = unfollowedUser.followers.toMutableList().apply { remove(followerUser) }
        unfollowedUser = unfollowedUser.copy(followers = updatedFollowersList)
        val updated = userRepository.save(followerUser)
        userRepository.save(unfollowedUser)
        return UserDTO(
            updated._id,
            userToUserInfoDTOConverter(updated),
            updated.following,
            updated.followers)
    }

    fun add(user: UserInfoDTO): User {
        val newUser = User(
            null,
            user.username,
            user.name,
            user.email,
            user.description,
            user.profilePictureFilePath,
            emptyList(),
            emptyList(),
            emptyList()
        )
        return userRepository.save(newUser)
    }

    fun findMyTravels(id: String): List<TravelDTO>? {
        val user = findById(id)
        return user.travels.map { travel ->
            TravelDTO(
                travel._id,
                travelService.travelToTravelInfoDto(travel),
                travel.participants,
                travel.public
            )}
    }

    fun addTravel(id: String, travel: Travel){
        var user = findById(id)
        val updatedTravelList = user.travels.toMutableList().apply { add(travel) }
        user = user.copy(travels = updatedTravelList)
        userRepository.save(user)
    }

    private fun userToUserInfoDTOConverter(user: User) : UserInfoDTO{
        return UserInfoDTO(user._id, user.username, user.name, user.email, user.description, user.profilePictureFilePath, user.travels)
    }
}