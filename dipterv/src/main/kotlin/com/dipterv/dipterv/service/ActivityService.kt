package com.dipterv.dipterv.service

import com.dipterv.dipterv.model.documentModel.activity.Activity
import com.dipterv.dipterv.repository.ActivityRepository
import org.springframework.stereotype.Service

@Service
class ActivityService(val repository: ActivityRepository) {
    fun findAllByTravelId(travelId: String): List<Activity>{
        return this.repository.findByTravelId(travelId)
    }

    fun deleteActivity(id: String){
        return this.repository.deleteById(id)
    }

    fun createActivity(activity: Activity): Activity{
        return this. repository.save(activity)
    }

//    fun copyActivity(id: String, travelId: String): Activity{
//        val activity = this.repository.findById(id).get()
//        activity.travelId=travelId
//        activity.id = null
//        activity.visited = false
//        return this.repository.save(activity)
//    }

    fun activityVisited(id: String): Activity{
        val activity = this.repository.findById(id).get()
        activity.visited = !activity.visited
        return this.repository.save(activity)
    }
}