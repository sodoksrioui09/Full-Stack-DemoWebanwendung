package com.example.demo.backend.security

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class JwtService (
    @Value("\${jwt.secret}") private val jwtSecret: String,

    @Value("\${jwt.expiration-ms}") private val jwtExpirationMs: Long

){
    private val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun generateToken(username:String,roles:Collection<String>): String {

        val now = Date()
        val expired = Date(now.time + jwtExpirationMs)
        val rolesStr = roles.joinToString(separator = ",")
        return Jwts.builder()
            .setSubject(username)
            .claim("roles",rolesStr)
            .setIssuedAt(now)
            .setExpiration(expired)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
    fun getUsernameFromToken(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).body.subject

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun getRolesFromToken(token: String): List<String> {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        val roles = claims["roles"] as String?
        return roles?.split(",") ?: emptyList()
    }
}