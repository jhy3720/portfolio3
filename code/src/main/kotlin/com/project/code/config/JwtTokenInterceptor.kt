package com.project.code


import io.jsonwebtoken.Claims
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

import com.project.code.common.JwtUtil
import io.jsonwebtoken.Jwts


/**
 *------------------------------------------------------------------------
 * 2023.05.11 최현우
 *
 * RESTAPI 통신이 들어왔을 때 인터셉트하여 들어오는 JWT 인터셉터를 구현하는 클래스
 * JWT토큰의 유효성을 검사한다.
 *------------------------------------------------------------------------
 */
@Component
class JwtTokenInterceptor() : HandlerInterceptor {

    /**
     *------------------------------------------------------------
     * 2023.05.11 최현우
     *
     * JWT 토큰 유효성 검증 함수
     *------------------------------------------------------------
     */
    private fun isTokenValid(remoteAddr: String?, requestTokenHeader: String, requestKey:String): Boolean {

        // 토큰이 Bearer로 시작하고 null이 아니라면
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            // substring(7) 메소드는 문자열에서 7번째 인덱스부터 끝까지의 문자열을 추출합니다.
            // 이는 "Bearer "를 제거하여 순수한 토큰 값을 추출하기 위한 작업
            val jwtToken = requestTokenHeader.substring(7).trim { it <= ' ' }

            try {

                // 토큰 파서 생성 후 Claims 얻기
                val parser = Jwts.parserBuilder().setSigningKey(JwtUtil.createKey(requestKey))
                val claims = parser.build().parseClaimsJws(jwtToken).body

                // JWT 토큰 내 userIP와 요청한 IP가 일치하는지 확인
                return checkIpAddress(remoteAddr, claims)

            } catch (e : Exception) {
                return false
            }
        }

        return false
    }

    /**
     *------------------------------------------------------------
     * 2023.05.11 최현우
     *
     * JWT 토큰의 IP 주소와 요청한 IP 주소가 일치하는지 확인하는 함수
     *------------------------------------------------------------
     */
    private fun checkIpAddress(remoteAddr: String?, claims: Claims): Boolean {

        val userIpInToken = claims["userIP"] as String
        // HTTP 요청에서 IP 주소 가져오기
        val ipAddress = remoteAddr

        return ipAddress == remoteAddr
    }


    /**
     *------------------------------------------------------------
     * 2023.05.11 최현우
     *
     * 요청이 컨트롤러에 전달되기 전 실행되는 메서드
     *------------------------------------------------------------
     * 2023.06.20 최현우
     *
     * getResume은 프론트 메인 화면 처음 오픈 시 사용됨으로
     * 토큰없이 통과할 수 있도록 하는 코드 추가.
     *------------------------------------------------------------
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        // 요청한 URI에서 Path 추출
        val path = request.requestURI.substringAfter('/')
        // 현재 사용자의 IP 주소 가져오기
        val remoteAddr = request.remoteAddr

        // 특정 URL 패턴에 대해서는 인증 과정을 거치지 않고 통과시킵니다.
        //cors로 인하여 통신요청 전 사전요청을 할때 사용하는 OPTIOMS는 토큰값을 가지고 있지 않기때문에 통과하도록 추가.
        //2023.06.30 getResume도 메인화면 최초 실행 시 사용됨으로 토큰없이 통과하도록 추가
        if (path == "restapis/login/loginToken" || request.method == "OPTIONS" || path == "restapis/resume/getResume" || path == "restapis/login/register") {
            return true
        }

        if(!isTokenValid(remoteAddr, request.getHeader("Authorization") ?: "", request.getHeader("Key") ?: "")){
            // 토큰 검증에 실패한 경우 401 Unauthorized 에러를 반환합니다.
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return false
        }

        // 토큰이 유효한 경우 다음으로 진행합니다.
        return true
    }
}
