package com.project.code
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 *------------------------------------------------------------------------
 * 2023.05.11 최현우
 *
 * 웹 설정에 사용되는 클래스
 * Cors 정책을 addCorsMappings 로, 인터셉터를 addInterceptors로 설정한다.
 *------------------------------------------------------------------------
 */
@Configuration
class WebConfig(val jwtInterceptor: JwtTokenInterceptor) : WebMvcConfigurer {

    //인터셉터를 등록하고, 어떤 패턴의 요청일때 인터셉터로 가는지 설정.
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/restapis/**")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowCredentials(true)
            .allowedMethods("*")
    }
}