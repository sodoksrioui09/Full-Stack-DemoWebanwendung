package com.example.demo.backend.repositories

import com.example.demo.backend.models.Task
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface TaskRepository :CrudRepository<Task,UUID>{
    fun findAllByOwnerUsername(username: String): List<Task>

}