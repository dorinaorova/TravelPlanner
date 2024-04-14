package com.dipterv.dipterv.controller

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.User
import com.dipterv.dipterv.model.dto.FollowDTO
import com.dipterv.dipterv.model.dto.UserDTO
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController
(val userService: UserService)
{

    @GetMapping("/all")
    fun getAllUser() : ResponseEntity<*>{
        val users = userService.getAll()
        return ResponseEntity(users, HttpStatus.OK)
    }

    @GetMapping("/findById/{id}")
    fun findUserById(@PathVariable("id") id: String)  : ResponseEntity<User>{
        try{
            val user = userService.findById(id)
            return ResponseEntity(user, HttpStatus.OK)
        }catch (e: NotFoundException){
            return ResponseEntity.notFound().build()
        }
        catch (e: Exception){
            return ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable("id") id: String, @RequestBody user: User) : ResponseEntity<UserInfoDTO>{
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

    @GetMapping("/name/{name}")
    fun findUsersByName(@PathVariable("name") name: String) : ResponseEntity<List<UserInfoDTO>>{
        val users = userService.findByName(name)
        return ResponseEntity(users, HttpStatus.OK)
    }

    @PostMapping("/add")
    fun addUser(@RequestBody user: UserInfoDTO) : ResponseEntity<User>{
        val newUser = userService.add(user)
        return ResponseEntity(newUser,HttpStatus.CREATED)
    }
}