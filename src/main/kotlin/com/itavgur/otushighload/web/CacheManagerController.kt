package com.itavgur.otushighload.web

import com.itavgur.otushighload.service.cache.CacheManagerService
import com.itavgur.otushighload.service.cache.CacheState
import com.itavgur.otushighload.web.dto.CacheManagerRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/manage/cache")
@Schema(description = "Cache management controller")
class CacheManagerController(
    @Autowired val cacheManagerService: CacheManagerService
) {

    @Operation(summary = "Get status of the cache", description = "Return 200 if ready, 202 otherwise")
    @GetMapping("/ready")
    fun getCacheState(@RequestBody(required = true) request: CacheManagerRequest): ResponseEntity<CacheState> =
        ResponseEntity(cacheManagerService.getCacheState(request), HttpStatus.OK)

    @PostMapping("/warm")
    @Operation(
        summary = "Start warming caches",
        description = "Warming cache, return 200 if started, 202 otherwise",
        hidden = true
    )
    fun warmCache(@RequestBody(required = true) request: CacheManagerRequest): ResponseEntity<HttpStatus> {
        return if (cacheManagerService.warmCache(request)) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.ACCEPTED)
        }

    }

    @Operation(summary = "Evict cache", description = "Evict cache", hidden = true)
    @PostMapping("/evict")
    fun deleteFriendById(
        @RequestBody(required = true) request: CacheManagerRequest
    ): ResponseEntity<HttpStatus> {
        return if (cacheManagerService.evictCache(request)) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.ACCEPTED)
        }
    }

}