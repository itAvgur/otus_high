package com.itavgur.otus.highload.app.web.hander

import com.itavgur.otus.highload.app.exception.*
import com.itavgur.otus.highload.app.util.logger
import com.itavgur.otus.highload.app.web.dto.GeneralErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ApplicationExceptionHandler {

    companion object {
        val LOG by logger()
    }

    @ExceptionHandler(NotImplementedError::class)
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    fun handleNotImplementedError(
        req: HttpServletRequest,
        exception: NotImplementedError
    ): ResponseEntity<Any> {
        LOG.error("Bad API - NotImplementedError : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.NOT_IMPLEMENTED, exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

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

    @ExceptionHandler(MessageNotFoundException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleMessageNotFoundException(
        req: HttpServletRequest,
        exception: MessageNotFoundException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - MessageNotFoundException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST, exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

}