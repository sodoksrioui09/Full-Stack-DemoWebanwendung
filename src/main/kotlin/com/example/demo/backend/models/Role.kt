package com.example.demo.backend.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.*
@Entity
class Role (
    @Id @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val name : String
)