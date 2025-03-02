package com.dipterv.dipterv.service

import com.dipterv.dipterv.model.documentModel.Ticket
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

@Service
class FileService {


    fun uploadFile(file: MultipartFile, uploadDirectory: Path, id: String) : String {
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory)
        }

        val fileName = generateFileName(file, id)
        val targetLocation: Path = uploadDirectory.resolve(fileName)
        file.transferTo(targetLocation)
        return fileName
    }

    fun downloadFile(folderPath: Path, name: String) :Resource {
        val filePath = folderPath.resolve(name)
        val fileResource: Resource = UrlResource(filePath.toUri())
        return fileResource
    }

    private fun generateFileName(file: MultipartFile, id: String): String{
        return "${id}_${LocalDate.now()}_${file.originalFilename}"
    }
}