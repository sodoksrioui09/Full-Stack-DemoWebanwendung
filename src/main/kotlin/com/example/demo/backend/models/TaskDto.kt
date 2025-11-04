package com.example.demo.backend.models

data class TaskDto(
    val title: String,
    val description: String,
    var status: String ,
    var createdAt: String ,
    var updatedAt: String ,
)