package com.androidlab.travelplannerapp.feature.uploadImage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.domain.usecases.image.UploadBackgroundUseCase
import com.androidlab.travelplannerapp.domain.usecases.image.UploadProfileUseCase
import com.androidlab.travelplannerapp.domain.usecases.image.UploadTravelImageUseCase
import com.androidlab.travelplannerapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.awaitResponse
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel@Inject constructor(
    private val uploadProfileUseCase: UploadProfileUseCase,
    private val uploadBackgroundUseCase: UploadBackgroundUseCase,
    private val uploadTravelImageUseCase: UploadTravelImageUseCase
) : ViewModel() {
    private var _id = mutableStateOf(null as String?)
    private var _imageUri = mutableStateOf(null as Uri?)
    private var _imagePath = mutableStateOf("")

    val imagePath: String
        get() = _imagePath.value
    var id: String?
        set(value:String?){
            _id.value = value
        }
        get(){
            return _id.value
        }

    var imageUri: Uri?
    set(value:Uri?){
        _imageUri.value = value
    }
    get(){
        return _imageUri.value
    }

    fun uploadImage(context: Context, uploadImageType: UploadImageType, navController: NavController) {
        viewModelScope.launch {
            var userId = _id.value
            if (_id.value == null) {
                val sharedPreferences =
                    context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
                userId = sharedPreferences.getString("id", null)
            }
            val contentResolver = context.contentResolver
            val uri = _imageUri.value
            if(uri != null) {
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    if (inputStream != null) {
                        val tempFile = File(context.cacheDir, "temp_upload_image.jpg")
                        tempFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }

                        val requestFile =
                            RequestBody.create("image/jpeg".toMediaTypeOrNull(), tempFile)
                        val body =
                            MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

                        val call = when (uploadImageType) {
                            UploadImageType.PROFILE -> uploadProfileUseCase(body, userId!!)
                            UploadImageType.BACKGROUND -> uploadBackgroundUseCase(body, userId!!)
                            UploadImageType.TRAVEL -> uploadTravelImageUseCase(body, userId!!)
                        }
                        val response = call?.awaitResponse()
                        if (response?.isSuccessful == true) {
                            tempFile.delete()
                            navController.navigate(Screen.ProfileScreen.route)
                        }
                    }

                }catch(e: Exception) {
                    Log.e("UploadImageViewModel", "Error while uploading image", e)
                }
            }else {
                Log.e("UploadImageViewModel", "Image URI is null")
            }

        }
    }

}
enum class UploadImageType {
    PROFILE,
    BACKGROUND,
    TRAVEL
}