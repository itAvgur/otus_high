package com.itavgur.otus.highload.app.service.cache

import com.itavgur.otus.highload.app.config.CacheConfig
import com.itavgur.otus.highload.app.exception.InvalidRequestException
import com.itavgur.otus.highload.app.util.logger
import com.itavgur.otus.highload.app.web.dto.CacheManagerRequest
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
        contex.getBeansOfType(CacheHandler::class.java).values.forEach {
            cacheHandlers.add(it)
        }
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