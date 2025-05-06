package com.dipterv.dipterv.controller

import com.dipterv.dipterv.model.documentModel.activity.Activity
import com.dipterv.dipterv.service.ActivityService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/activity")
class ActivityController (val activityService: ActivityService) {
    @GetMapping("/travel/{id}")
    fun getActivitiesForTravel(@PathVariable id: String): ResponseEntity<List<Activity>> {
        val activities = activityService.findAllByTravelId(id)
        return ResponseEntity.ok(activities)
    }

    @PostMapping
    fun createActivity(@RequestBody activity: Activity): ResponseEntity<Activity>{
        return ResponseEntity(this.activityService.createActivity(activity), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteActivity(@PathVariable id: String): ResponseEntity<String>{
        this.activityService.deleteActivity(id)
        return ResponseEntity.ok("deleted")
    }
    @GetMapping("/visit/{id}")
    fun visitActivity(@PathVariable id: String): ResponseEntity<Activity>{
        return ResponseEntity.ok(this.activityService.activityVisited(id))
    }
}