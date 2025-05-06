package com.androidlab.travelplannerapp.feature.uploadImage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.FileOutputStream

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
                        val currentTimeInMillis: Long = System.currentTimeMillis()
                        val tempFile = File(context.cacheDir, "temp_upload_image.jpg")

                        tempFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                        val resizedFile = resizeImage(tempFile, File(context.cacheDir, "${currentTimeInMillis}.jpg"), 1080, 1080)

                        val requestFile =
                            RequestBody.create("image/jpeg".toMediaTypeOrNull(), resizedFile)
                        val body =
                            MultipartBody.Part.createFormData("file", resizedFile.name, requestFile)

                        val call = when (uploadImageType) {
                            UploadImageType.PROFILE -> uploadProfileUseCase(body, userId!!)
                            UploadImageType.BACKGROUND -> uploadBackgroundUseCase(body, userId!!)
                            UploadImageType.TRAVEL -> uploadTravelImageUseCase(body, _id.value!!)
                        }
                        val response = call?.awaitResponse()
                        if (response?.isSuccessful == true) {
                            tempFile.delete()
                            resizedFile.delete()
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

    private fun resizeImage(imageFile: File, outputFile: File, maxWidth: Int, maxHeight: Int): File {
        // Decode the file to a Bitmap
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        // Calculate the scaling factor
        val scaleFactor = Math.min(
            options.outWidth / maxWidth,
            options.outHeight / maxHeight
        )

        options.inJustDecodeBounds = false
        options.inSampleSize = scaleFactor

        // Decode the file with the scaling factor
        val resizedBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        // Save the resized bitmap to the output file
        val outputStream = FileOutputStream(outputFile)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        outputStream.flush()
        outputStream.close()

        return outputFile
    }

}
enum class UploadImageType {
    PROFILE,
    BACKGROUND,
    TRAVEL
}