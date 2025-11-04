package com.example.demo.backend.services

import com.example.demo.backend.models.Task
import com.example.demo.backend.repositories.TaskRepository
import org.springframework.stereotype.Service
import java.util.*
@Service
class TaskServiceImp (val taskRepository: TaskRepository) : TaskService {
    override fun getAllTasks(): List<Task> = taskRepository.findAll().toList()
    override fun getTaskById(id: UUID): Task = taskRepository.findById(id).orElse(null)
    override fun saveTask(task: Task): Task = taskRepository.save(task)
    override fun updateTask(id: UUID, updatedTask: Task): Task {
        return taskRepository.findById(id).map{
            it.title= updatedTask.title
            it.status=updatedTask.status
            it.description=updatedTask.description
            it.createdAt=updatedTask.createdAt
            it.updatedAt=updatedTask.updatedAt
            taskRepository.save(updatedTask)
        }.orElse(null)
    }
    override fun deleteTask(id: UUID): Boolean {
        return if (taskRepository.existsById(id)){
            taskRepository.deleteById(id)
            true
        }else{
            false
        }
    }
}