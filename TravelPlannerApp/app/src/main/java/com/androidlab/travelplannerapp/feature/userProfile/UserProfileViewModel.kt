package com.androidlab.travelplannerapp.feature.userProfile

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.FollowRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.user.FollowUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.UnfollowUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import com.androidlab.travelplannerapp.feature.utils.isFollower
import com.androidlab.travelplannerapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase
) : ViewModel() {
    private var _user = mutableStateOf(UserInfo("","","","","","", "", emptyList(), "", "", emptyList(), emptyList()))

    val user: UserInfo
        get() = _user.value

    fun logout(context: Context, navController: NavController){
        val sharedPreferences = context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        navController.navigate(Screen.LoginScreen.route){
            popUpTo(0)
        }
    }

    fun loadUserData(_id: String?, context: Context){
        viewModelScope.launch {
            var id = _id
            if (id == null) {
                val sharedPreferences =
                    context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
                id = sharedPreferences.getString("id", null)
            }
            val call = getUserDataUseCase(id!!)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _user.value=response.body()!!
            }
        }
    }

    fun backgroundPicturePath(context: Context): String{
        val BASE_URL = context.getString(R.string.BASE_URL)
        return BASE_URL+"user/image/background/"+user.backgroundPictureFilePath
    }

    fun followAction(context: Context){
        viewModelScope.launch {
            var call: Call<UserInfo>? = null
            val followRequest = FollowRequest(followerId = getOwnUserId(context)!!, followedId = user._id!!)
            if (isFollower(user.followerIds, context)) {
                call = unfollowUseCase(followRequest)
            } else {
                call = followUseCase(followRequest)
            }
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _user.value=response.body()!!
            }
        }
    }

}