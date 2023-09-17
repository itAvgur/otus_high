package com.itavgur.otushighload.aspect

import com.itavgur.otushighload.service.CredentialService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes


@Component
@Aspect
class AuthenticationAspect(
    @Autowired val credentialService: CredentialService
) {

    companion object {
        const val TOKEN_PARAMETER_NAME = "token"
    }

    @Pointcut("@annotation(com.itavgur.otushighload.aspect.Authenticated)")
    fun authenticatedAnnotation() {
        //nothing here, just for annotation
    }

    @Around("authenticatedAnnotation()")
    @Throws(Throwable::class)
    fun authenticated(jp: ProceedingJoinPoint): Any? {
        checkConditional()
        return jp.proceed()
    }

    @Throws(Throwable::class)
    private fun checkConditional() {

        val servletRequestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val token = servletRequestAttributes.request.getParameter(TOKEN_PARAMETER_NAME)
        credentialService.checkAuthentication(token)

    }
}