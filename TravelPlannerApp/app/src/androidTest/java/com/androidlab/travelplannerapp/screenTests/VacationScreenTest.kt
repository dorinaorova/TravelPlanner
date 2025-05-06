package com.androidlab.travelplannerapp.screenTests

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.androidlab.travelplannerapp.MainActivity
import com.androidlab.travelplannerapp.MockWebServerManager
import com.androidlab.travelplannerapp.TestConfig
import com.androidlab.travelplannerapp.data.model.LoginResponse
import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.SpendType
import com.androidlab.travelplannerapp.data.model.Transaction
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
class VacationScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val userData = UserInfo("test", "test_user", "test_name", "test_email", "test", null, null,
        listOf("id"),"test_city","test_country",listOf("1"),listOf("2"), null)
    val otherUserData = UserInfo("test2", "test_user2", "test_name2", "test_email2", "", null, null,
        listOf("id2"),"test_city2","test_country2", emptyList(),emptyList(), null)

    val travel = Travel("id", "test vacation", 1767225600000, 1767225600000, "test", "test", ownerId = "test",public = true,description = "test",participantIds = listOf("test2"))
    val transaction = Transaction("test", "test2", 10.0)
    val spends = Payment("spendId", 1767225600000, "test", listOf("test2"),10.0, SpendType.SETTLEMENT, "id")
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
            .putString("refresh_token", "test-refresh-token")
            .putString("id", "test")
            .apply()

        val customDispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                println(request.path)
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
                    request.path?.startsWith("/travel/id") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(travel))
                    }
                    request.path?.startsWith("/travel/participate/") == true  -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(emptyList<Travel>()))
                    }
                    request.path?.equals("/user/findById/test") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(userData))
                    }
                    request.path?.equals("/user/findById/test2") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(otherUserData))
                    }
                    request.path?.equals("/user/all") == true -> {
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
                    request.path?.startsWith("/spends/transaction/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(listOf(transaction)))
                    }
                    request.path?.startsWith("/spends/travel/") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Gson().toJson(listOf(spends)))
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
    fun navigateToVacationScreen(){
        composeTestRule.onNodeWithTag("upcomingVacation").performClick()
        composeTestRule.onNodeWithTag("vacationScreen").assertIsDisplayed()
    }

    @Test
    fun vacationDataDisplayedCorrectly(){
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("upcomingVacation", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("vacationName", useUnmergedTree = true).assertTextEquals(travel.name)
        composeTestRule.onNodeWithTag("addMember", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("vacation_participant_test", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("vacation_participant_test2", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("payments", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("transaction", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("transaction_fromUser", useUnmergedTree = true).assertTextEquals("You")
        composeTestRule.onNodeWithTag("transaction_amount", useUnmergedTree = true).assertTextEquals("10.0")
        composeTestRule.onNodeWithTag("transaction_toUser", useUnmergedTree = true).assertTextEquals("test_user2")
        composeTestRule.onNodeWithTag("ownDept", useUnmergedTree = true).assertTextEquals("10.0")
        composeTestRule.onNodeWithTag("plan", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("tickets", useUnmergedTree = true).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun navigateToPaymentsScreen() {
        composeTestRule.onNodeWithTag("upcomingVacation").performClick()
        composeTestRule.onNodeWithTag("payments").performClick()
        composeTestRule.onNodeWithTag("paymentsScreen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("topbar_title").assertTextEquals("Payments")

    }

    @Test
    fun navigateToInvitationScreen() {
        composeTestRule.onNodeWithTag("upcomingVacation").performClick()
        composeTestRule.onNodeWithTag("addMember",useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("invitationScreen").assertIsDisplayed()
    }

    @Test
    fun navigateToPlanScreen() {
        composeTestRule.onNodeWithTag("upcomingVacation").performClick()
        composeTestRule.onNodeWithTag("plan").performClick()
        composeTestRule.onNodeWithTag("activityListScreen").assertIsDisplayed()
    }

    @Test
    fun navigateToTicketsScreen() {
        composeTestRule.onNodeWithTag("upcomingVacation").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tickets", useUnmergedTree = true).performScrollTo().performClick()
        composeTestRule.onNodeWithTag("ticketsScreen").assertIsDisplayed()
    }

}