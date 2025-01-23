package com.dipterv.dipterv.controller

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.FollowDTO
import com.dipterv.dipterv.model.dto.UserDTO
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.model.requestModel.UserUpdateRequest
import com.dipterv.dipterv.service.FileService
import com.dipterv.dipterv.service.UserService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths

@RestController
@RequestMapping("/user")
class UserController
(val userService: UserService, private val fileService: FileService,)
{

    @GetMapping("/all")
    fun getAllUser(
        @RequestParam(required = false) name: String?
    ) : ResponseEntity<*>{
        var users = userService.getAll()
        name?.let{
            users= userService.nameFilter(name, users)
        }
        return ResponseEntity(users, HttpStatus.OK)
    }

    @GetMapping("/findById/{id}")
    fun findUserById(@PathVariable("id") id: String)  : ResponseEntity<UserInfoDTO>{
        try{
            val user = userService.findUserInfoDTOById(id)
            return ResponseEntity(user, HttpStatus.OK)
        }catch (e: NotFoundException){
            return ResponseEntity.notFound().build()
        }
        catch (e: Exception){
            return ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable("id") id: String, @RequestBody user: UserUpdateRequest) : ResponseEntity<UserInfoDTO>{
        try{
            val updatedUser = userService.updateUser(id, user)
            return ResponseEntity(updatedUser, HttpStatus.OK)
        }catch(e: NotFoundException){
            return ResponseEntity.notFound().build()
        }catch (e: Exception){
            return ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/follow")
    fun follow(@RequestBody follow: FollowDTO): ResponseEntity<UserDTO>{
        try{
            val updatedUser = userService.followUser(follow)
            return ResponseEntity(updatedUser, HttpStatus.OK)
        }catch(e: NotFoundException){
            return ResponseEntity.notFound().build()
        }catch (e: Exception){
            return ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/unfollow")
    fun unfollow(@RequestBody follow: FollowDTO): ResponseEntity<UserDTO>{
        try{
            val updatedUser = userService.unfollowUser(follow)
            return ResponseEntity(updatedUser, HttpStatus.OK)
        }catch(e: NotFoundException){
            return ResponseEntity.notFound().build()
        }catch (e: Exception){
            return ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/add")
    fun addUser(@RequestBody user: UserInfoDTO) : ResponseEntity<User>{
        val newUser = userService.add(user)
        return ResponseEntity(newUser,HttpStatus.CREATED)
    }
    @PostMapping("/image/upload/{id}/profile")
    fun uploadProfilePicture(@RequestPart("file") file: MultipartFile, @PathVariable("id") id: String) : ResponseEntity<*> {
        val fileName = fileService.uploadFile(file, Paths.get("user/profile"), id)
        val updatedUser = userService.uploadProfilePicture(id, fileName)
        return ResponseEntity.ok(updatedUser)
    }
    @PostMapping("/image/upload/{id}/background")
    fun uploadBackgroundPicture(@RequestPart("file") file: MultipartFile, @PathVariable("id") id: String) : ResponseEntity<*> {
        val fileName = fileService.uploadFile(file, Paths.get("user/background"), id)
        val updatedUser = userService.uploadBackgroundPicture(id, fileName)
        return ResponseEntity.ok(updatedUser)
    }

    @GetMapping("image/background/{name}")
    fun loadBackgroundPicture(@PathVariable("name") name: String) : ResponseEntity<Resource> {
        val image = fileService.downloadFile( Paths.get("user/background"), name)
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        return ResponseEntity.ok().headers(headers).body(image)
    }

    @GetMapping("image/profile/{name}")
    fun loadProfilePicture(@PathVariable("name") name: String) : ResponseEntity<Resource> {
        val image = fileService.downloadFile( Paths.get("user/profile"), name)
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        return ResponseEntity.ok().headers(headers).body(image)
    }
}