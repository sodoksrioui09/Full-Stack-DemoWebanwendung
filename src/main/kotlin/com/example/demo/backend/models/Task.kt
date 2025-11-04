package com.example.demo.backend.models

import jakarta.persistence.*
import org.springframework.boot.autoconfigure.security.SecurityProperties
import java.time.Instant
import java.util.UUID
@Entity
 data class Task (
  @Id
    val id : UUID = UUID.randomUUID(),
  var title: String = "",
  var description: String = "",
  var status: String = "",
  var createdAt: String = "",
  var updatedAt: String = "",
  @ManyToOne
   @JoinColumn(name = "user_id")
   val owner : User? =null,
)