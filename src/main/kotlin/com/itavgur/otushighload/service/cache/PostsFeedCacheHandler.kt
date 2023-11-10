package com.itavgur.otushighload.service.cache

import com.itavgur.otushighload.config.CacheConfig
import com.itavgur.otushighload.service.PostService
import com.itavgur.otushighload.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

@Component
@ConditionalOnBean(CacheConfig::class)
@ConditionalOnProperty("cache.post-feed.enabled", havingValue = "true", matchIfMissing = false)
class PostsFeedCacheHandler(
    @Autowired private val userService: UserService
) : CacheHandler {

    companion object {
        const val NAME_OF_CACHE = "post-feed"
    }

    @Value("\${cache.post-feed.warm-on-start:false}")
    private val warmOnStart: Boolean = false

    protected val warmLock: ReadWriteLock = ReentrantReadWriteLock()
    protected val postFeedWarmQueue: Queue<Int> = LinkedList()

    protected val evictLock: ReadWriteLock = ReentrantReadWriteLock()
    protected val postFeedEvictQueue: Queue<Int> = LinkedList()

    override fun name(): String {
        return NAME_OF_CACHE
    }

    override fun initCache() {
        if (warmOnStart) warmCache(emptySet())
    }

    override fun warmCache(usersId: Set<Int>): Boolean {

        val writeLock = warmLock.writeLock()
        return if (writeLock.tryLock(CacheManagerService.DEFAULT_TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
            if (usersId.isEmpty()) {
                userService.getTopActiveUsers().forEach { postFeedWarmQueue.add(it.id) }
            } else {
                usersId.forEach { postFeedWarmQueue.add(it) }
            }
            writeLock.unlock()
            true
        } else {
            false
        }
    }

    override fun evictCache(usersId: Set<Int>): Boolean {

        val writeLock = evictLock.writeLock()
        return if (writeLock.tryLock(CacheManagerService.DEFAULT_TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
            usersId.forEach { postFeedEvictQueue.add(it) }
            writeLock.unlock()
            true
        } else {
            false
        }
    }

    override fun getState(): CacheState {

        if (postFeedWarmQueue.isNotEmpty()) return CacheState.WARMING

        if (!warmLock.readLock().tryLock()) return CacheState.WARMING

        warmLock.readLock().unlock()
        return CacheState.READY
    }

    @Component
    @ConditionalOnBean(PostsFeedCacheHandler::class)
    @EnableScheduling
    class PostsFeedCacheScheduler(
        @Autowired private val postService: PostService,
        @Autowired private val cacheHandler: PostsFeedCacheHandler
    ) {

        @Scheduled(cron = "\${cache.post-feed.cron}")
        fun warmPostFeedCache() {

            val readLock = cacheHandler.warmLock.readLock()

            if (!readLock.tryLock()) return

            CacheManagerService.LOG.debug("Start warming cache")

            while (cacheHandler.postFeedWarmQueue.size > 0) {
                postService.feedPosts(userId = cacheHandler.postFeedWarmQueue.poll())
            }

            CacheManagerService.LOG.debug("Finish warming cache")

            readLock.unlock()
        }

        @Scheduled(cron = "\${cache.post-feed.cron}")
        fun evictPostFeedCache() {

            val readLock = cacheHandler.evictLock.readLock()

            if (!readLock.tryLock()) return

            CacheManagerService.LOG.debug("Start evicting cache")

            while (cacheHandler.postFeedEvictQueue.size > 0) {
                postService.feedPosts(userId = cacheHandler.postFeedEvictQueue.poll())
            }

            CacheManagerService.LOG.debug("Finish evicting cache")

            readLock.unlock()
        }
    }
}