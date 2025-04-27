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
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
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
class ProfileScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val userData = UserInfo("test", "test_user", "test_name", "test_email", "test", null, null,
        listOf("id"),"test_city","test_country",listOf("1"),listOf("2"), null)
    val travels = listOf(Travel("id", "test vacation", 1767225600000, 1767225600000, "test", "test"))

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
        val sharedPrefs = context.getSharedPreferences("refresh_token", Context.MODE_PRIVATE)

        sharedPrefs.edit()
            .putString("refresh_token_key", "your_token_value")
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
                    request.path?.startsWith("/travel/user/") == true  -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(travels))
                    }
                    request.path?.startsWith("/user/") == true && request.method == "PUT" -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(userData))
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
    fun profileScreenDisplayed(){
        composeTestRule.onNodeWithTag("homeScreen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("profile_navItem").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("profile_screen").assertIsDisplayed()
    }

    @Test
    fun profileDataDisplayedCorrectly(){
        composeTestRule.onNodeWithTag("profile_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("profile_screen").assertIsDisplayed()

        composeTestRule.onNodeWithTag("profile_name").assertTextEquals(userData.name)
        composeTestRule.onNodeWithTag("profile_username").assertTextEquals(userData.username)
        composeTestRule.onNodeWithTag("profile_email").assertTextEquals(userData.email)
        composeTestRule.onNodeWithTag("profile_livingLabel").assertTextEquals("${userData.city}, ${userData.country}")
        composeTestRule.onNodeWithTag("profile_description").assertTextEquals(userData.description?:"...")
        composeTestRule.onNodeWithTag("profile_follower", useUnmergedTree = true).assertTextEquals("1")
        composeTestRule.onNodeWithTag("profile_following", useUnmergedTree = true).assertTextEquals("1")
        composeTestRule.onNodeWithTag("profile_travel", useUnmergedTree = true).assertTextEquals("1")
        composeTestRule.onNodeWithTag("profile_travelitem_add").assertIsDisplayed()
        composeTestRule.onNodeWithTag("profile_travelitem_id").assertIsDisplayed()
    }

    @Test
    fun profileUpdateScreenDisplayed(){
        composeTestRule.onNodeWithTag("profile_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("profile_dropdownMenu").performClick()
        composeTestRule.onNodeWithTag("profile_dropdownMenu_edit").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("user_update_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("user_update_name").assertTextEquals(userData.name)
        composeTestRule.onNodeWithTag("user_update_email").assertTextEquals(userData.email)
        composeTestRule.onNodeWithTag("user_update_description").assertTextEquals(userData.description?:"")
        composeTestRule.onNodeWithTag("user_update_city").assertTextEquals(userData.city?:"")
        composeTestRule.onNodeWithTag("user_update_country").assertTextEquals(userData.country?:"")
        composeTestRule.onNodeWithTag("user_update_save").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("profile_screen").assertIsDisplayed()
    }

}