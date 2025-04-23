package com.androidlab.travelplannerapp

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import com.androidlab.travelplannerapp.feature.login.LoginScreen
import com.androidlab.travelplannerapp.navigation.Navigation
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(ApiUseCaseModule::class)
class LoginScreenTest {
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    private lateinit var mockWebServer: MockWebServer
    lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        val context = ApplicationProvider.getApplicationContext<Context>()
        context.getSharedPreferences("refresh_token", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            Navigation(navController = navController)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testLoginScreen() {
        val username = "test"
        val password = "test"

        /*TODO*/
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{"accessToken": "fakeAccess"}""")
        mockWebServer.enqueue(mockResponse)

        composeTestRule.setContent {
            LoginScreen(navController)
        }

        composeTestRule.onNodeWithTag("username").assertIsDisplayed()


        composeTestRule.onNodeWithTag("password").assertIsDisplayed()
            //.performTextInput(password)

        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
           // .performClick()

    }
}
