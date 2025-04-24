package com.androidlab.travelplannerapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(ApiUseCaseModule::class)
class LoginScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mockWebServerManager: MockWebServerManager

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServerManager = MockWebServerManager
        mockWebServerManager.start()

//        val context = ApplicationProvider.getApplicationContext<Context>()
//        context.getSharedPreferences("refresh_token", Context.MODE_PRIVATE)
//            .edit()
//            .clear()
//            .commit()



//        mockWebServerManager.enqueueResponse(
//            MockResponse()
//                .setResponseCode(200)
//                .setBody("Mocked login response")
//        )
    }

    @After
    fun teardown() {
        mockWebServerManager.shutdown()
    }
    @Test
    fun testLoginScreen() {
        val username = "test"
        val password = "test"

        composeTestRule.onNodeWithTag("username").assertIsDisplayed()


        composeTestRule.onNodeWithTag("password").assertIsDisplayed()
            //.performTextInput(password)

        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
           // .performClick()

    }
}
