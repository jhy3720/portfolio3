package com.project.code.common

//import javax.crypto.spec.SecretKeySpec
import java.security.Key

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import java.util.UUID

import io.jsonwebtoken.security.Keys
//import javax.crypto.SecretKey


object JwtUtil {
    //private const val TOKEN_SECRET = "yoursecretkey"
    private const val TOKEN_EXPIRATION = 31536000000 // 토큰 만료 시간 (1년), 밀리초 단위
    //private val key: SecretKey = generateKey()


     /**
     *------------------------------------------------------------------------
     * 2023.06.12 최현우
     *
     * 네트워크 통신 시 헤더에 추가되어 사용되는 토큰값을 신규발급/재발급 하는 메서드
     *------------------------------------------------------------------------
     */
    fun refreshToken(user: Any, partnum: String, userIP: String): String {
        val now = System.currentTimeMillis()

        val key = createKey(partnum);

        val claims = Jwts.claims()
            .setSubject(user.toString())

        claims["userIP"] = userIP

        return Jwts.builder()
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(Date(now))
            .setExpiration(Date(now + TOKEN_EXPIRATION))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     *------------------------------------------------------------------------
     * 2023.06.12 최현우
     *
     * 네트워크 통신 시 헤더에 전달되는 토큰값이 유효한지 대해 검사하는 메서드
     *------------------------------------------------------------------------
     */
    fun verify(jwt: String, Key: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(createKey(Key))
                .build()
                .parseClaimsJws(jwt)
                .body

            claims["userIP"] != null
        } catch (e: Exception) {
            false
        }
    }


    //fun generateKey() = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    /**
     *------------------------------------------------------------------------
     * 2023.06.12 최현우
     *
     * 토큰을 생성할 때 사용되는 키값을 생성하는 메서드
     *------------------------------------------------------------------------
     */
    fun createKey(keyValue: String): Key {
        // 사용자 정의 값으로 key 생성
        return Keys.hmacShaKeyFor(keyValue.toByteArray())

    }

}