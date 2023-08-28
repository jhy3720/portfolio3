package com.project.code.common



import com.auth0.jwt.algorithms.Algorithm


object JwtInfo {
    const val HEADER_NAME = "jwt-header"
    const val ISSUER = "LIFE"
    const val TOKEN_KEY = "sksmsdixndxnddlrhfahreowkd"
    const val EXPIRES_LIMIT: Long = 1L

    fun getAlgorithm(): Algorithm {
        return try {
            Algorithm.HMAC256(TOKEN_KEY)
        } catch (e: IllegalArgumentException) {
            Algorithm.none()
        }
    }
}
