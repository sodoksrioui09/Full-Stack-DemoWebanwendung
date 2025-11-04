/*package com.example.demo.controllers

import com.example.demo.models.Task
import com.example.demo.services.TaskService
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping(produces = [MediaType.TEXT_HTML_VALUE])
class TaskController(private val taskService: TaskService) {

    @GetMapping("/tasks")
    fun getAllTasks(model: Model): String {
        val allTasks = taskService.getAllTasks()
        model.addAttribute("tasks", allTasks)
        return "tasks" // -> templates/tasks.html
    }

    @GetMapping("/tasks/{id}")
    fun getTaskById(@PathVariable id: UUID, model: Model): String {
        val task = taskService.getTaskById(id)
        model.addAttribute("task", task)
        return "task" // -> templates/task.html
    }

    @PostMapping("/tasks")
    fun saveTask(
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam status: String,
        @RequestParam createdAt: String,
        @RequestParam updatedAt: String
    ): String {
        val task = Task().apply {
            this.title = title
            this.description = description
            this.status = status
            this.createdAt = createdAt
            this.updatedAt = updatedAt
        }
        taskService.saveTask(task)
        return "redirect:/tasks"
    }

    @PostMapping("/tasks/{id}/update")
    fun updateTask(
        @PathVariable id: UUID,
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam status: String,
        @RequestParam createdAt: String,
        @RequestParam updatedAt: String
    ): String {
        val task = taskService.getTaskById(id)
        task.let {
            it.title = title
            it.description = description
            it.status = status
            it.createdAt = createdAt
            it.updatedAt = updatedAt
            taskService.saveTask(it)
        }
        return "redirect:/tasks"
    }

    @PostMapping("/tasks/{id}/delete")
    fun deleteTask(@PathVariable id: UUID): String {
        taskService.deleteTask(id)
        return "redirect:/tasks"
    }
}*/