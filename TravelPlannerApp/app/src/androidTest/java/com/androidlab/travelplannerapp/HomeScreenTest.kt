package com.androidlab.travelplannerapp

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import com.google.gson.Gson
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
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
            mockWebServerManager = MockWebServerManager
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

        val context = ApplicationProvider.getApplicationContext<Context>()
        val sharedPrefs = context.getSharedPreferences("refresh_token", Context.MODE_PRIVATE)

        sharedPrefs.edit()
            .putString("refresh_token_key", "your_token_value")
            .apply()

        mockWebServerManager.enqueueResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody("true")
        )
    }

    @Test
    fun homeScreenAutomaticallyAppearsIfUserPreviouslyLoggedIn(){
        composeTestRule.onNodeWithTag("homeScreen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("profile_navItem").assertIsDisplayed()
    }

    @Test
    fun theUpcomingVacationsAndInvitationsAppearOnHomeScreen(){
        val travels = listOf(Travel("id", "test vacation", 1767225600000, 1767225600000, "test", "test"))
        mockWebServerManager.enqueueResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(travels))
        )
        mockWebServerManager.enqueueResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(travels))
        )

        val invitations = listOf(Invitation("id", "test", "test"))
        mockWebServerManager.enqueueResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(invitations))
        )
        mockWebServerManager.enqueueResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(travels))
        )

        composeTestRule.onNodeWithTag("upcomingVacation").assertIsDisplayed()
        composeTestRule.onNodeWithText("You are on vacation").assertIsDisplayed()

        composeTestRule.onNodeWithTag("invitations").assertIsDisplayed()
        composeTestRule.onNodeWithTag("id_invitation").assertIsDisplayed()
    }

}