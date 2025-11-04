package com.example.demo.backend.repositories

import com.example.demo.backend.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository :CrudRepository<User,UUID>{
    fun findByUsername(username: String): Optional<User>
    fun existsByUsername(username: String): Boolean

}