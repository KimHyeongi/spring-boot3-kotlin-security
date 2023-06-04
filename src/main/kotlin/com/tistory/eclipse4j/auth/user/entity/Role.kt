package com.tistory.eclipse4j.auth.user.entity

import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import java.util.stream.Collectors


enum class Role(
    val permissions: Set<Permission>
) {
    USER(Collections.emptySet<Permission>()),
    ADMIN(
        setOf<Permission>(
            Permission.ADMIN_READ,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_CREATE,
            Permission.MANAGER_READ,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_CREATE
        )
    ),
    MANAGER(
        setOf<Permission>(
            Permission.MANAGER_READ,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_CREATE
        )
    );

    open fun getAuthorities(): MutableList<SimpleGrantedAuthority> {
        val authorities: MutableList<SimpleGrantedAuthority> = permissions
            .stream()
            .map { permission -> SimpleGrantedAuthority(permission.permission) }
            .collect(Collectors.toList())
        authorities.add(SimpleGrantedAuthority("ROLE_$name"))
        return authorities
    }
}
