package com.example.demo.backend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter (
    private val jwtService: JwtService,
    private val customUserDetailsService: CustomUserDetailsService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        val token = header.substring(7)
        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response)
            return
        }
        val username = jwtService.getUsernameFromToken(token)
        val userDetails = customUserDetailsService.loadUserByUsername(username)
        val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        auth.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = auth
        filterChain.doFilter(request, response)
    }
}