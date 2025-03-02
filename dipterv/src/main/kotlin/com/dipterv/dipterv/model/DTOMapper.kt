package com.dipterv.dipterv.model

import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.TravelDTO
import com.dipterv.dipterv.model.dto.TravelInfoDTO
import com.dipterv.dipterv.model.dto.UserDTO
import com.dipterv.dipterv.model.dto.UserInfoDTO
import org.springframework.stereotype.Service

@Service
class DTOMapper {
    
    fun userToUserDTO(user: User): UserDTO {
        return UserDTO(
            user._id,
            userToUserInfoDTO(user))
    }

    fun userToUserInfoDTO(user: User): UserInfoDTO {
        return UserInfoDTO(
            user._id,
            user.username,
            user.name,
            user.email,
            user.description,
            user.profilePictureFilePath,
            user.backgroundPictureFilePath,
            user.travelIds,
            user.country,
            user.city,
            user.followingIds,
            user.followerIds
        )
    }

}