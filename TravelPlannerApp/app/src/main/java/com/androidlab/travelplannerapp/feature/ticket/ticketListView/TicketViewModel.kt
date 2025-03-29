package com.androidlab.travelplannerapp.feature.ticket.ticketListView

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Ticket
import com.androidlab.travelplannerapp.domain.usecases.ticket.CreateTicketUseCase
import com.androidlab.travelplannerapp.domain.usecases.ticket.DownloadTicketFileUseCase
import com.androidlab.travelplannerapp.domain.usecases.ticket.GetTicketsByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.ticket.UploadTicketFileUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.awaitResponse
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val getTicketsByTravelIdUseCase: GetTicketsByTravelIdUseCase,
    private val createTicketUseCase: CreateTicketUseCase,
    private val downloadTicketFileUseCase: DownloadTicketFileUseCase,
    private val uploadTicketFileUseCase: UploadTicketFileUseCase
) : ViewModel() {
    private val _tickets = mutableStateListOf<Ticket>()
    private var travelId: String = ""

    val tickets: List<Ticket>
        get() = _tickets

    fun setTravelId(id: String){
        travelId = id
    }

    fun fetchData(){
        viewModelScope.launch {
            val call = getTicketsByTravelIdUseCase(travelId)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _tickets.clear()
                _tickets.addAll(response.body()!!)
                _tickets.sortBy { it.date }
            }
        }
    }

    fun downloadTicket(fileName: String, context: Context){
        viewModelScope.launch {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val localFile = File(downloadsDir, fileName)

            if (localFile.exists()) {
                // File already exists, open it directly
                openPdfFile(context, localFile)
            }else{
            val call = downloadTicketFileUseCase(fileName)
                val response = call?.awaitResponse()
                if (response?.isSuccessful == true) {
                    response.body()?.let { responseBody ->
                        saveFileToStorage(responseBody, fileName)
                        openPdfFile(context, localFile)
                    }
                }
            }
        }
    }

    private fun openPdfFile(context: Context, pdfFile: File) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error opening file: ${e.message}")
        }
    }

    private fun saveFileToStorage(responseBody: ResponseBody, fileName: String) {
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) // Save under Downloads directory
        val file = File(storageDir, fileName)

        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            inputStream = responseBody.byteStream()
            outputStream = FileOutputStream(file)

            val buffer = ByteArray(4096)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    fun fileExists(fileName: String): Boolean {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val localFile = File(downloadsDir, fileName)
        return localFile.exists()
    }

    fun createTicket(name: String, date: Long, context: Context){
        val ticket = Ticket(name=name, date=date, userId = getOwnUserId(context)!!, travelId = travelId, files = emptyList())
        viewModelScope.launch {
            val call = createTicketUseCase(ticket)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                fetchData()
            }

        }
    }

    fun uploadTicket(uri: Uri, context: Context, ticketId: String){
        viewModelScope.launch {
            val originalFilename = queryFileName(context, uri) ?: Date().toString()

            val contentResolver = context.contentResolver
            val inputStream: InputStream = contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Unable to open InputStream from the provided Uri.")

            val tempFile = File(context.cacheDir, originalFilename)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            val requestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), tempFile)
            val body =MultipartBody.Part.createFormData("file", tempFile.name, requestBody)
            val call = uploadTicketFileUseCase(body, ticketId)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                fetchData()
            }
        }
    }

    fun queryFileName(context: Context, uri: Uri): String? {
        return if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(
                uri, null, null, null, null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                } else null
            }
        } else {
            uri.path?.let { File(it).name }
        }
    }
}

