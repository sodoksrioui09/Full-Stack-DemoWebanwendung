package com.example.demo.backend.services

import com.example.demo.backend.models.Task
import java.util.*
interface TaskService {
    fun getAllTasks(): List<Task>
    fun getTaskById(id: UUID): Task
    fun saveTask(task: Task): Task
    fun updateTask(id: UUID,updatedTask: Task): Task
    fun deleteTask(id: UUID): Boolean

}