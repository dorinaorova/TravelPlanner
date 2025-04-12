package com.dipterv.dipterv

import com.dipterv.dipterv.model.documentModel.activity.Activity
import com.dipterv.dipterv.model.documentModel.activity.ActivityType
import com.dipterv.dipterv.repository.ActivityRepository
import com.dipterv.dipterv.service.ActivityService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.Optional

class ActivityServiceTest {

    private val  activityRepository:  ActivityRepository = mockk()
    private val activityService = ActivityService(activityRepository)
    private val activity = Activity("testId","name", ActivityType.RESTAURANT, "travelId", false, null, null)

    @Test
    fun whenActivitySetVisited_VisitedValueNegated(){
        val updatedActivity = Activity("testId","name", ActivityType.RESTAURANT, "travelId", true, null, null)
        every {activityRepository.findById("testId")} returns Optional.of(activity)
        every {activityRepository.save(updatedActivity)} returns updatedActivity

        activityService.activityVisited("testId")

        verify { activityRepository.save(updatedActivity) }
    }


}