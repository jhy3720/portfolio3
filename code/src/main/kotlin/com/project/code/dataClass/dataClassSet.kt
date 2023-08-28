package com.project.code.dataClass
import org.springframework.core.io.Resource
import com.project.code.entity.Account
import com.project.code.entity.UserData
import java.util.*

/**
 *------------------------------------------------------------------------
 * 2023.06.12 최현우
 *
 * 네트워크 통신에 사용되는 정보들을 DataClass 형태로 구성하는 클래스
 *------------------------------------------------------------------------
 */
class dataClassSet {

    //front > back 최초로그인 시 사용되는 정보
    data class UserInfo(
        val userId: String,
        val userPw: String,
        val userIP: String,
        val userKey: String?
    )

    //로그인 데이터를 통해 DB에서 유저정보, JPA사용을 위해 Entity로 변경함.
    //    data class Account(
    //        val accountId: String,
    //        val accountPw: String,
    //        val accountIP: String,
    //        val accountKey: String
    //    )

    //로근 시 조회한 유저정보와 토큰값을 front로 리턴하기 위해 사용하는 정보
    data class UserDataAndToken(

        val accountData: Optional<Account>,
        val userData: Optional<UserData>,
        val token: String,

        )


    /**
     * -----------------------------------------------------------------------------------------------
     * 회원 가입 시 front 에서 전달하는 정보 (저장 시 Account, UserData 각각맞는 데이터로 분리하여 작업)
     * -----------------------------------------------------------------------------------------------
     * 2023.07.17 최현우
     * 
     * accEnDate 변수명을 accEndate 와 같이 작성하여 'D'를 소문자로 넣어버려 API명세서와 달라 파싱문제 생기는 부분 
     * 수정
     * -----------------------------------------------------------------------------------------------
     */
    data class RegisterInfo(

        val id : String,
        val password : String,

        val email : String,
        val cellphone : String,
        val name : String,
        val nickname : String,

        val work: String,

        val accLoginDate: String,
        val accStDate: String,
        val accEnDate: String,
        val accPwChangeDate: String


    )

    //프로필 정보
    data class ProfileInfo(
        val email : String,
        val cellphone : String,
        val work: String,
        val workDetail: String,
        val nicName: String

    )
    //프로필 조회 시 사용되는 사용자 이메일 정보
    data class ProfileGetData (
        val email : String
    )

    //이력서 정보 조회시 사용되는 조회 내용 정보
    data class ResumeGetInfo (

        val selectType : String,
        val serchData : String
    )


    //이력서 정보
    data class ResumeInfo (

        val thumbNail: MutableList<Resource>,
        val resumeNum: Array<String>
    )

}
