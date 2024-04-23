package com.dipterv.dipterv.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    fun handlerNotFoundException(e: NotFoundException): ResponseEntity<NotFoundException> {
        return ResponseEntity(e, HttpStatus.NOT_FOUND)
    }
}