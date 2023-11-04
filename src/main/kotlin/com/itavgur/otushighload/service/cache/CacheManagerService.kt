package com.itavgur.otushighload.service.cache

import com.itavgur.otushighload.config.CacheConfig
import com.itavgur.otushighload.exception.InvalidRequestException
import com.itavgur.otushighload.util.logger
import com.itavgur.otushighload.web.dto.CacheManagerRequest
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(CacheConfig::class)
class CacheManagerService(
    @Autowired private val contex: ApplicationContext
) {

    val cacheHandlers: MutableSet<CacheHandler> = mutableSetOf()

    companion object {
        val LOG by logger()
        const val DEFAULT_TRY_LOCK_TIMEOUT = 10000L
    }

    @PostConstruct
    fun init() {

        cacheHandlers.add(contex.getBean(PostsFeedCacheHandler::class.java))
    }

    fun warmCache(request: CacheManagerRequest): Boolean {

        val handler = cacheHandlers.firstOrNull { it.name() == request.cacheName }

        handler?.let {
            handler.warmCache(request.ids)
            return true
        }
        throw InvalidRequestException("handler ${request.cacheName} not registered")
    }


    fun evictCache(request: CacheManagerRequest): Boolean {
        val handler = cacheHandlers.firstOrNull { it.name() == request.cacheName }

        handler?.let {
            handler.evictCache(request.ids)
            return true
        }
        throw InvalidRequestException("handler ${request.cacheName} not registered")
    }

    fun getCacheState(request: CacheManagerRequest): CacheState {
        val handler = cacheHandlers.firstOrNull { it.name() == request.cacheName }

        handler?.let {
            return handler.getState()
        }

        throw InvalidRequestException("handler ${request.cacheName} not registered")
    }

}