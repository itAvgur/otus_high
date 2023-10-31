package com.itavgur.otushighload.web.hander

import com.itavgur.otushighload.exception.CredentialException
import com.itavgur.otushighload.exception.InvalidRequestException
import com.itavgur.otushighload.exception.PostNotFoundException
import com.itavgur.otushighload.exception.UserNotFoundException
import com.itavgur.otushighload.util.logger
import com.itavgur.otushighload.web.dto.GeneralErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(InvalidRequestException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleInvalidRequestException(
        req: HttpServletRequest,
        exception: InvalidRequestException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - InvalidRequestException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST, exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(CredentialException::class)
    fun handleCredentialException(
        req: HttpServletRequest,
        exception: CredentialException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - CredentialException : ${exception.message}")
        return when (exception.httpCode) {
            HttpStatus.UNAUTHORIZED -> {
                val response = GeneralErrorResponse(HttpStatus.UNAUTHORIZED, exception.message, null)
                ResponseEntity(response, HttpStatus.UNAUTHORIZED)
            }

            else -> {
                val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST, exception.message, null)
                ResponseEntity(response, HttpStatus.BAD_REQUEST)
            }
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(
        req: HttpServletRequest,
        exception: MethodArgumentNotValidException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - MethodArgumentNotValidException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST, exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    companion object {
        val LOG by logger()
    }

    @ExceptionHandler(PostNotFoundException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handlePostNotFoundException(
        req: HttpServletRequest,
        exception: PostNotFoundException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - PostNotFoundException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST, exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleUserNotFoundException(
        req: HttpServletRequest,
        exception: UserNotFoundException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - UserNotFoundException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST, exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

}