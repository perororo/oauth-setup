package com.blog.resourceserver

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class HelloService {
    @PreAuthorize("hasAuthority('SCOPE_user.read')")
    fun hello(): String {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userSubject = jwt.subject
        return "Hello: ${userSubject}!"
    }
}