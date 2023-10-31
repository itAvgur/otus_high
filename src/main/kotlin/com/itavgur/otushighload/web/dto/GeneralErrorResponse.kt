package com.itavgur.otushighload.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(name = "ResponseError", description = "Wrapper for the error response")
data class GeneralErrorResponse<T>(
    @Schema(name = "code", required = true)
    val code: HttpStatus,
    @Schema(name = "message", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val message: String? = null,
    @Schema(name = "response", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val response: T? = null
)