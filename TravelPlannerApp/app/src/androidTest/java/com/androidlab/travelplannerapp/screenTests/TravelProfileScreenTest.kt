package com.androidlab.travelplannerapp.screenTests

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.androidlab.travelplannerapp.MainActivity
import com.androidlab.travelplannerapp.MockWebServerManager
import com.androidlab.travelplannerapp.TestConfig
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.ActivityType
import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import com.androidlab.travelplannerapp.feature.utils.calculateDays
import com.androidlab.travelplannerapp.feature.utils.generateDate
import com.google.gson.Gson
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(ApiUseCaseModule::class)
class TravelProfileScreenTest  {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val userData = UserInfo("test", "test_user", "test_name", "test_email", "test", null, null,
        listOf("id"),"test_city","test_country",listOf("1"),listOf("2"), null)

        val travel =Travel("id", "test vacation", 1767225600000, 1767225600000, "test", "test", ownerId = "test")

    companion object {
        lateinit var mockWebServerManager: MockWebServerManager

        @BeforeClass
        @JvmStatic
        fun startServer() {
            mockWebServerManager = MockWebServerManager
            MockWebServerManager.start()
            TestConfig.mockBaseUrl = MockWebServerManager.getBaseUrl()
        }

        @AfterClass
        @JvmStatic
        fun stopServer() {
            MockWebServerManager.shutdown()
        }
    }

    @Before
    fun setup() {
        hiltRule.inject()

        val context = ApplicationProvider.getApplicationContext<Context>()
        val sharedPrefsRefreshToken = context.getSharedPreferences("refresh_token", Context.MODE_PRIVATE)
        val sharedPrefsUserId = context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)

        sharedPrefsRefreshToken.edit()
            .putString("refresh_token_key", "your_token_value")
            .apply()

        sharedPrefsUserId.edit()
            .putString("id", "test")
            .apply()

        MockWebServerManager.enqueueResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody("true")
        )

        val customDispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path?.startsWith("/user/findById/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(userData))
                    }
                    request.path?.startsWith("/travel/id") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(travel))
                    }
                    request.path?.startsWith("/travel/user/") == true  -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(listOf(travel)))
                    }
                    request.path?.startsWith("/activity/travel/") == true -> {
                        MockResponse().setResponseCode(200).setBody(Gson().toJson(emptyList<Activity>()))
                    }
                    else -> {
                        MockResponse().setResponseCode(404)
                    }
                }
            }
        }
        MockWebServerManager.setDispatcher(customDispatcher)
    }

    @Test
    fun navigateToTravelProfile(){
        composeTestRule.onNodeWithTag("profile_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("profile_travelitem_id").assertIsDisplayed()
        composeTestRule.onNodeWithTag("profile_travelitem_id").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("travel_profile_screen").assertIsDisplayed()
    }

    @Test
    fun travelInfoDisplayedCorrectly(){
        composeTestRule.onNodeWithTag("profile_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("profile_travelitem_id").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("travel_profile_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("travel_name").assertTextEquals(travel.name)
        composeTestRule.onNodeWithTag("travel_location").assertTextEquals("${travel.city}, ${travel.country}")
        composeTestRule.onNodeWithTag("travel_price").assertTextEquals("${travel.price} ${travel.currency}")
        composeTestRule.onNodeWithTag("travel_date").assertTextEquals("${
            calculateDays(
                travel.startDate,
                travel.endDate
            )
        } days (${generateDate(travel.startDate)} - ${generateDate(travel.endDate)})")
        composeTestRule.onNodeWithTag("travel_description").assertTextEquals(travel.description?:"...")
    }
}