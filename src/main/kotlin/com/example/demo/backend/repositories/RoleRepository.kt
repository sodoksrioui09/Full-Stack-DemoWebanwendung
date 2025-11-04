package com.example.demo.backend.repositories

import com.example.demo.backend.models.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository :CrudRepository<Role,UUID>{
    fun findByName(name: String): Optional<Role>

}