package com.itavgur.otus.highload.app.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(name = "cacheManagerRequest", description = "Request for cache manager")
data class CacheManagerRequest(
    @field:NotBlank
    @Schema(name = "cacheName", required = true)
    var cacheName: String,
    @Schema(name = "ids", required = false)
    var ids: Set<Int> = emptySet()

)