package com.tistory.eclipse4j.auth.user.service

import com.tistory.eclipse4j.auth.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserFindService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return checkNotNull(userRepository.findByEmail(username)){"사용자를 찾을 수 없습니다."}
    }
}