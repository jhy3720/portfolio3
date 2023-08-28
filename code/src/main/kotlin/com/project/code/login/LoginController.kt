package com.project.code.login

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.project.code.common.JwtUtil
import org.json.JSONObject

import com.project.code.common.JwtInfo
import com.project.code.dataClass.dataClassSet

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import com.project.code.entity.Account
import com.project.code.entity.UserData
import java.util.Optional

/**
 *------------------------------------------------------------------------
 * 2023.05.11 최현우
 *
 * 로그인과 관련된 요청을 받는 컨트롤러
 *------------------------------------------------------------------------
 * LoginDao에서 ropositoy를 사용하려면 클래스 생성자에서 인자값으로 주입한 LoginService
 * 를 받아야 함으로 해당부분 추가.
 *------------------------------------------------------------------------
 * 2023.07.10 최현우
 *
 * Dao에서  @Autowired으로 사용 가능하여서 해당 생성자에서 객체 의존성 주입하는부분 수정
 * ----------------------------------------------------------------------------------
 */
@RestController
@RequestMapping("/restapis/login",produces = [MediaType.APPLICATION_JSON_VALUE])

class LoginController() {

    @Autowired
    private var loginService : LoginService = LoginServiceImpl();
    /**
     *------------------------------------------------------------------------
     * 2023.05.15 최현우
     *
     * 로그인 & JWT토큰 발행 메서드
     *------------------------------------------------------------------------
     * 2023.06.12 최현우 수정
     * 
     * 최종 리턴값에 들어가는 정보들이 KEY 값 명칭변경.
     *------------------------------------------------------------------------
     * 2023.07.07 최현우
     *
     * 토큰 값 및 DB에서 가져오는 유저 어카운트정보를 실제 DB에서 조회하도록 수정하면서
     * 리턴받는 값이 data class 에서 Optinal<Acoount>로 바뀌게 되었고, 해당데이터를
     * json으로 변환하는 부분의 코드를 변경함.
     *------------------------------------------------------------------------
     * 2023.07.12 최현우
     *
     * GetToken()에서 리턴받는 데이터를 계정정보, 유저정보로 나누었기 떄문에  accountData,
     * userData를 파싱하여 추가하는 코드작성.
     * 추가적으로 프론트에서 일반적인 API사용 시 추가해야하는 Key 값도 accountKey로
     * 전달하도록 코드 추가.
     *------------------------------------------------------------------------
     * 2023.07.30 최현우
     *
     * 로그인 시도한 계정을 통해서 값을 못가져오거나 예외가 발생했을 때 처리하는 로직 추가.
     *------------------------------------------------------------------------
     */
    @PostMapping("/loginToken")
    fun loginToken(@RequestBody userInfo: dataClassSet.UserInfo): ResponseEntity<String> {

        //토큰을 받아온다.
        val data = loginService.GetToken(userInfo);

        val accountData : Optional<Account>
        val userData : Optional<UserData>
        var token : String? = null;

        if (data != null) {

            accountData = data.accountData
            userData = data.userData
            token = data.token

        }else{

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("LoginFail")
        }

        // AccessToken 유효성 검증
        return if (token != null && JwtUtil.verify(token, accountData.get().GetAssembleAccKey())) {


            // DataClass 객체를 JSON 문자열로 변환합니다. 이렇게 하는이유는 제이슨으로 보내지않으면
            //데이터 클래스는 문자열로 통으로 인식하여 프론트에서 data.userId이런식으로 사용이 안됨.
            //val objectMapper = jacksonObjectMapper()
            //val userDataJson = objectMapper.writeValueAsString(userData)

            // Account 객체를 JSON 문자열로 변환합니다.
            val objectMapper = ObjectMapper()
            val accountDataJson = objectMapper.writeValueAsString(accountData.orElse(null))

            // Account JSON 문자열을 JSONObject로 변환합니다.
            val accountObj = JSONObject(accountDataJson)


            // UserData 객체를 JSON 문자열로 변환합니다.
            val userDataJson = objectMapper.writeValueAsString(userData.orElse(null))

            // UserData JSON 문자열을 JSONObject로 변환합니다.
            val userDataObj = JSONObject(userDataJson)

            accountObj.put("accountKey", accountData.get().GetAssembleAccKey())

            //리턴 제이슨 객체 생성.
            val resultObj = JSONObject().apply{
                put("CODE", 200)
                put("MESSAGE", "success")
                put("TOKEN", token)
                put("ACCOUNTDATA", accountObj)
                put("USERDATA", userDataObj)

            }

            //바디와 해더에 넣어 리턴
            ResponseEntity.ok()
                .header(JwtInfo.HEADER_NAME, token)
                .body(resultObj.toString())

        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed")

        }
    }



    /**
     *------------------------------------------------------------------------
     * 2023.06.12 최현우
     *
     * 회원가입 정보 저장 메서드
     *------------------------------------------------------------------------
     */
    @PostMapping("/register")
    fun register(@RequestBody data : dataClassSet.RegisterInfo): ResponseEntity<String> {

        try{

            val isSucceed = loginService.Register(data)

            //리턴 제이슨 객체 생성.
            val resultObj = JSONObject().apply{
                put("code", 200)
                put("IsSucceed", isSucceed)

            }

            //바디와 해더에 넣어 리턴
            return ResponseEntity.ok()
                    .body(resultObj.toString())


        }catch ( e : Exception){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed")

        }


    }
}