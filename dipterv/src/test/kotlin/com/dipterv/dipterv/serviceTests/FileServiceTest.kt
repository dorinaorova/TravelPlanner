package com.dipterv.dipterv.serviceTests

import com.dipterv.dipterv.service.FileService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDate

class FileServiceTest {

    val fileService = FileService()

    @Test
    fun testFileNameGenerate(){
        val fileContent = "This is a test file".toByteArray()
        val mockFile = MockMultipartFile(
            "file",               // name of the parameter
            "originalName.txt",   // original filename
            "text/plain",         // content type
            fileContent           // content
        )
        val generatedFileName = fileService.generateFileName(mockFile, "testId")
        val expectedFileName = "testId_${LocalDate.now()}_originalName.txt"
        assertEquals(generatedFileName, expectedFileName)
    }
}