package com.example.demo.backend.controllers

import com.example.demo.backend.models.*
import com.example.demo.models.*
import com.example.demo.backend.repositories.RoleRepository
import com.example.demo.backend.repositories.UserRepository
import com.example.demo.backend.security.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController (
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
){
    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<Any> {
        if (userRepository.existsByUsername(req.username)) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Username already exists"))
        }
        val user = User(username = req.username, password = passwordEncoder.encode(req.password))
        // assign default role USER
        val userRole = roleRepository.findByName("USER").orElseGet {
            roleRepository.save(Role(name = "USER"))
        }
        user.roles.add(userRole)
        userRepository.save(user)
        return ResponseEntity.ok(mapOf("message" to "User registered"))
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<Any> {
        val userOpt = userRepository.findByUsername(req.username)
        if (userOpt.isEmpty) return ResponseEntity.status(401).body(mapOf("error" to "Invalid credentials"))
        val user = userOpt.get()
        if (!passwordEncoder.matches(req.password, user.password)) {
            return ResponseEntity.status(401).body(mapOf("error" to "Invalid credentials"))
        }
        val roles = user.roles.map { it.name }
        val token = jwtService.generateToken(user.username, roles)
        return ResponseEntity.ok(AuthResponse(token))
    }

}