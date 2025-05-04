package com.androidlab.travelplannerapp.screenTests

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.androidlab.travelplannerapp.MainActivity
import com.androidlab.travelplannerapp.MockWebServerManager
import com.androidlab.travelplannerapp.TestConfig
import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.model.LoginResponse
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
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
class HomeScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    companion object {
        lateinit var mockWebServerManager: MockWebServerManager

        @BeforeClass
        @JvmStatic
        fun startServer() {
            mockWebServerManager = MockWebServerManager()
            mockWebServerManager.start()
            TestConfig.mockBaseUrl = mockWebServerManager.getBaseUrl()
        }

        @AfterClass
        @JvmStatic
        fun stopServer() {
            mockWebServerManager.shutdown()
        }
    }

    @Before
    fun setup() {
        hiltRule.inject()

        val context = getInstrumentation().targetContext
        val sharedPrefs = context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)

        sharedPrefs.edit()
            .putString("id", "test")
            .apply()

        val travel = Travel("id", "test vacation", 1767225600000, 1767225600000, "test", "test", ownerId = "test",public = true,description = "test",participantIds = listOf("test2"))
        val invitations = listOf(Invitation("id", "test", "test"))
        val customDispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path?.startsWith("/travel/user/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(listOf(travel)))
                    }
                    request.path?.startsWith("/travel/all") == true  -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(listOf(travel)))
                    }
                    request.path?.startsWith("/travel/participate/") == true  -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(emptyList<Travel>()))
                    }
                    request.path?.startsWith("/invitation/user/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(invitations))
                    }
                    request.path?.startsWith("/auth/login") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(LoginResponse("test", "test", "test")))
                    }
                    request.path?.startsWith("/auth/refresh-token/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(false))
                    }
                    else -> {
                        MockResponse().setResponseCode(404)
                    }
                }
            }
        }
        mockWebServerManager.setDispatcher(customDispatcher)
        composeTestRule.onNodeWithTag("loginBtn").performClick()
    }

    @Test
    fun homeScreenAutomaticallyAppearsIfUserPreviouslyLoggedIn(){
        composeTestRule.onNodeWithTag("homeScreen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("profile_navItem").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_navItem").assertIsDisplayed()
        composeTestRule.onNodeWithTag("search_navItem").assertIsDisplayed()
    }

    @Test
    fun theUpcomingVacationsAndInvitationsAppearOnHomeScreen(){
        composeTestRule.onNodeWithTag("upcomingVacation").assertIsDisplayed()
        composeTestRule.onNodeWithText("You are on vacation").assertIsDisplayed()

        composeTestRule.onNodeWithTag("invitations").assertIsDisplayed()
        composeTestRule.onNodeWithTag("id_invitation").assertIsDisplayed()
    }

}