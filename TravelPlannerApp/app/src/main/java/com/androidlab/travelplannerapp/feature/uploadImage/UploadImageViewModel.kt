package com.androidlab.travelplannerapp.feature.uploadImage

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel@Inject constructor() : ViewModel() {
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

    fun uploadImage(context: Context){
        var userId = _id.value
        if(_id.value == null){
        val sharedPreferences =
            context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
        userId= sharedPreferences.getString("id", null)
        }

        val file = File(_imageUri.value!!.path ?: "")
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

}
enum class UploadImageType {
    PROFILE,
    BACKGROUND,
    TRAVEL
}