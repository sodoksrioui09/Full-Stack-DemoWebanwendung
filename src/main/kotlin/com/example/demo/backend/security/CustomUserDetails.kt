package com.example.demo.backend.security

import com.example.demo.backend.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails (private val user : User): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        user.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }.toMutableList()

    override fun getPassword() = user.password
    override fun getUsername() = user.username
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}