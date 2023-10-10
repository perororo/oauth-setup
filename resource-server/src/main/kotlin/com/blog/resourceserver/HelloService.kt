package com.blog.resourceserver

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class HelloService {
    @PreAuthorize("hasAuthority('SCOPE_user.read')")
    fun hello(): String {
        val someUserInfo = SecurityContextHolder.getContext().authentication.principal
        return "Hello: ${someUserInfo}!"
    }
}