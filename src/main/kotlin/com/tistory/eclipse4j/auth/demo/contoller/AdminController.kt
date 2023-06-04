package com.tistory.eclipse4j.auth.demo.contoller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController {
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    fun get(): String {
        return "GET:: admin controller"
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    fun post(): String {
        return "POST:: admin controller"
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    fun put(): String {
        return "PUT:: admin controller"
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    fun delete(): String {
        return "DELETE:: admin controller"
    }
}