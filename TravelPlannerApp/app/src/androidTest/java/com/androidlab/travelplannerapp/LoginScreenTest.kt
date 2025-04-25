package com.androidlab.travelplannerapp

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(ApiUseCaseModule::class)
class LoginScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    companion object {
    lateinit var mockWebServerManager: MockWebServerManager

        @BeforeClass @JvmStatic
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
        context.getSharedPreferences("refresh_token", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()



        mockWebServerManager.enqueueResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody("false")
        )
    }
//
//    @After
//    fun teardown() {
//        mockWebServerManager.shutdown()
//    }
    @Test
    fun testLoginScreen() {
        composeTestRule.onNodeWithTag("username").assertIsDisplayed()
        composeTestRule.onNodeWithTag("password").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loginBtn").assertIsDisplayed()
    }
//    @Test
//    fun testRegistrationScreen(){
//        composeTestRule.onNodeWithText("Sign up here").assertIsDisplayed().performClick()
//        composeTestRule.onNodeWithTag("username").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("name").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("email").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("password").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("registerBtn").assertIsDisplayed()
//
//    }
//    @Test
//    fun testLoginFunction() {
//        val username = "test"
//        val password = "test"
//        composeTestRule.onNodeWithTag("username").performTextInput(username)
//        composeTestRule.onNodeWithTag("password").performTextInput(password)
//        composeTestRule.onNodeWithTag("loginBtn").performClick()
//
//
//    }
}
