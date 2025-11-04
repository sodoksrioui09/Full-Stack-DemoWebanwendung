package com.example.demo.backend.models

import jakarta.persistence.*
import java.util.*
@Entity
@Table(name = "app_user")
 data class User (
    @Id
    val id : UUID=UUID.randomUUID(),
    val username : String,
    var password : String,
    @ManyToMany(fetch = FetchType.EAGER)
    val roles: MutableList<Role> = mutableListOf()

)
