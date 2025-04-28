package com.androidlab.travelplannerapp.screenTests

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.androidlab.travelplannerapp.MainActivity
import com.androidlab.travelplannerapp.MockWebServerManager
import com.androidlab.travelplannerapp.TestConfig
import com.androidlab.travelplannerapp.data.model.LoginResponse
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
class SearchScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val userData = UserInfo("test", "test_user", "test_name", "test_email", "test", null, null,
        listOf("id"),"test_city","test_country",listOf("1"),listOf("2"), null)
    val otherUserData = UserInfo("test2", "test_user2", "test_name2", "test_email2", "", null, null,
        listOf("id2"),"test_city2","test_country2", emptyList(),emptyList(), null)

    val ownTravel = Travel("id", "test vacation", 1767225600000, 1767225600000, "test", "test", ownerId = "test",public = true,description = "test",)
    val otherTravel = Travel("id2", "test vacation2", 1767225600000, 1767225600000, "test", "test", ownerId = "test2",public = true,description = "test",)
    val filterValues = listOf(200,400,2,12)
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

        val context = getInstrumentation().targetContext
        val sharedPrefs = context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)

        sharedPrefs.edit()
            .putString("refresh_token", "test-refresh-token")
            .putString("id", "test")
            .apply()

        val customDispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                println(request.path)
                return when {
                    request.path?.startsWith("/travel/filterValues") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(filterValues))
                    }
                    request.path?.startsWith("/user/findById/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(userData))
                    }
                    request.path?.startsWith("/travel/all") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(listOf(ownTravel, otherTravel)))
                    }
                    request.path?.startsWith("/user/all") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(listOf(userData, otherUserData)))
                    }
                    request.path?.startsWith("/auth/refresh-token/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody("false")
                    }
                    request.path?.startsWith("/auth/login") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(LoginResponse("test", "test", "test")))
                    }
                    else -> {
                        MockResponse().setResponseCode(404)
                    }
                }
            }
        }
        MockWebServerManager.setDispatcher(customDispatcher)
        composeTestRule.onNodeWithTag("loginBtn").performClick()
    }

    @Test
    fun navigateToSearchScreen(){
        composeTestRule.onNodeWithTag("search_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("search_screen").assertIsDisplayed()
    }

    @Test
    fun travelsDisplayedOnTravelSearchScreen(){
        composeTestRule.onNodeWithTag("search_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("travel_listitem_id").assertIsDisplayed()
        composeTestRule.onNodeWithTag("travel_listitem_id2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("travel_listitem_id2").onChildren()
            .filter(hasTestTag("like_button"))
    }

    @Test
    fun usersDisplayedOnUserSearchScreen(){
        composeTestRule.onNodeWithTag("search_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("user_picker").performClick()
        composeTestRule.onNodeWithTag("user_listitem_test").assertIsDisplayed()
        composeTestRule.onNodeWithTag("user_listitem_test2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("user_listitem_test2").onChildren()
            .filter(hasTestTag("like_button"))
    }

    @Test
    fun filterDialogDisplayed(){
        composeTestRule.onNodeWithTag("search_navItem").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("filter_btn").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("filter_dialog").assertIsDisplayed()
    }
}