package com.example.demo.backend.controllers

import com.example.demo.backend.models.Task
import com.example.demo.backend.models.TaskDto
import com.example.demo.backend.repositories.TaskRepository
import com.example.demo.backend.repositories.UserRepository
import com.example.demo.backend.services.TaskService
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {
    @GetMapping
    fun listAll(@AuthenticationPrincipal principal: UserDetails): List<Task> {
        val username = principal.username
        val user = userRepository.findByUsername(username).get()
        // normal user -> only own tasks
        // admin -> all tasks
        return if (user.roles.any { it.name == "ADMIN" }) {
            taskRepository.findAll().toList()
        } else {
            taskRepository.findAllByOwnerUsername(user.username)
        }
    }

    @PostMapping
    fun create(@RequestBody dto: TaskDto, @AuthenticationPrincipal principal: UserDetails): Task {
        val user = userRepository.findByUsername(principal.username).get()
        val task = Task(title = dto.title, description = dto.description, owner = user,status = dto.status,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt)
        return taskRepository.save(task)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody dto: TaskDto, @AuthenticationPrincipal principal: UserDetails): ResponseEntity<Any> {
        val user = userRepository.findByUsername(principal.username).get()
        val taskOpt = taskRepository.findById(id)
        if (taskOpt.isEmpty) return ResponseEntity.notFound().build()
        val task = taskOpt.get()
        if (task.owner?.id != user.id && user.roles.none { it.name == "ADMIN" }) {
            return ResponseEntity.status(403).body(mapOf("error" to "Forbidden"))
        }
        task.title = dto.title
        task.description = dto.description
        task.updatedAt = Instant.now().toString()
        return ResponseEntity.ok(taskRepository.save(task))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID, @AuthenticationPrincipal principal: UserDetails): ResponseEntity<Any> {
        val user = userRepository.findByUsername(principal.username).get()
        val taskOpt = taskRepository.findById(id)
        if (taskOpt.isEmpty) return ResponseEntity.notFound().build()
        val task = taskOpt.get()
        if (task.owner?.id != user.id && user.roles.none { it.name == "ADMIN" }) {
            return ResponseEntity.status(403).body(mapOf("error" to "Forbidden"))
        }
        taskRepository.delete(task)
        return ResponseEntity.ok(mapOf("message" to "Deleted"))
    }
}
