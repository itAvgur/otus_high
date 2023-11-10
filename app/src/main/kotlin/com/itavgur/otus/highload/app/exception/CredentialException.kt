package com.itavgur.otus.highload.app.exception

import org.springframework.http.HttpStatus

class CredentialException(override val message: String?, val httpCode: HttpStatus? = null) : RuntimeException(message)