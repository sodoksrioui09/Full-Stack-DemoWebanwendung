package com.example.demo.backend.config

import com.example.demo.backend.models.Role
import com.example.demo.backend.models.User
import com.example.demo.backend.repositories.RoleRepository
import com.example.demo.backend.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Check if admin already exists
        if (userRepository.count() == 0L) {
            val adminRole = roleRepository.findByName("ADMIN")
                .orElseGet { roleRepository.save(Role(name = "ADMIN")) }

            val adminUser = User(
                username = "admin",
                password = passwordEncoder.encode("admin123"),
                roles = mutableListOf(adminRole)
            )
            userRepository.save(adminUser)
            println("✅ Created initial ADMIN user: admin / admin123")
        }
    }
}