package com.project.code.login

import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.project.code.common.JwtUtil
import com.project.code.dataClass.dataClassSet
import com.project.code.entity.Account
import com.project.code.entity.UserData
import java.util.*

/**
* ----------------------------------------------------------------------------------
* 2023.07.07 최현우
*
* 디비 repository를 사용하는 dao를 사용하려면 LoginServiceImpl의 생성자 부분에
* private val loginDao: LoginDao를 추가해야 정상작동하기 때문에 인자값 추가.
* ----------------------------------------------------------------------------------
* 2023.07.10 최현우
*
* Dao에서  @Autowired으로 사용 가능하여서 해당 생성자에서 객체 의존성 주입하는부분 수정
* ----------------------------------------------------------------------------------
*/
@Service
class LoginServiceImpl() : LoginService{

    @Autowired
    private var loginDao : LoginDao = LoginDao();
    /**
    * ----------------------------------------------------------------------------------
    * 2023.07.07 최현우
    *
    * 로그인 시 리턴하는 계정 정보를 실제 DB에서 조회한 값으로 들어가도록 변경함.
    * 리턴 타입도 Optional<Account>로 변경.
    * ----------------------------------------------------------------------------------
    * 2023.07.20 최현우
    *
    * 데이터를 가져오지 못했을 때 예외 로직추가
    * ----------------------------------------------------------------------------------
    */
    override fun GetAccountData(userId : String, userPw : String, userIP : String): Optional<Account> {

        val account : Optional<Account>  = loginDao.GetAccountData(userId, userPw)

        if(account.isEmpty) {

            throw IllegalArgumentException("Load Fail Account")

        }else{

            return loginDao.GetAccountData(userId, userPw)

        }

    }

    /**
    * ----------------------------------------------------------------------------------
    * 2023.07.12 최현우
    *
    * 로그인 시 유저 정보를 리턴하는 메서드.
    * ----------------------------------------------------------------------------------
    * 2023.07.20 최현우
    *
    * 데이터를 가져오지 못했을 때 예외 로직추가
    * ----------------------------------------------------------------------------------
    */
    override fun GetUserData(userSeq : Long): Optional<UserData> {

        val userData :  Optional<UserData> =  loginDao.GetUserData(userSeq)

        if(userData.isEmpty) {

            throw IllegalArgumentException("Load Fail UserData")

        }else{

            return userData

        }



    }



    /**
    * ----------------------------------------------------------------------------------
    * 2023.07.07 최현우
    *
    * account의 타입을 Optional<Account>로 변경
    * ----------------------------------------------------------------------------------
    * 2023.07.12 최현우
    *
    * 로그인 시 유저정보를 따로 가져와 리턴해주는 부분 추가
    * ---------------------------------------------------------------------------------
    * 2023.07.20 최현우
    *
    * 로직 처리중에 데이터를 불러오지 못하거나 예외가 발생한 경우를 처리하기위해 로직 수정 및
    *  try문 안으로 코드 이동.
    * ----------------------------------------------------------------------------------
    */
    override fun GetToken (userInfo : dataClassSet.UserInfo) : dataClassSet.UserDataAndToken?{


        val userId = userInfo.userId
        val userIP = userInfo.userIP

        var userPwd = userInfo.userPw

        // 비밀번호 암호화
        try {

            userPwd =  DigestUtils.sha512Hex(userInfo.userPw)

            // Account 정보 가져오기
            val account : Optional<Account> = GetAccountData(userId,userPwd,userIP);

            //유저 정보 가져오기
            val userData : Optional<UserData> = GetUserData(account.get().GetSeqNum());

            //뭔가 키값? 혹은 계정별로 가진 값? (요양기관번호 같은..)
            val accKey = account.get().GetAssembleAccKey();

            // AccessToken 생성
            val token = JwtUtil.refreshToken(account, accKey, userIP)


            return  dataClassSet.UserDataAndToken(account,userData,token)

        } catch (e: Exception) {
            e.printStackTrace()
            userPwd = ""
        }

        return null;
    }

    /**
     * ----------------------------------------------------------------------------------
     * 2023.07.12 최현우
     *
     * 회원 가입 시 정보를 등록하는 메서드.
     * ----------------------------------------------------------------------------------
     */
    override fun Register (registerInfo : dataClassSet.RegisterInfo) : Boolean{

        try{

            var isSuccessSaveed : Boolean =   loginDao.Register(registerInfo)
            return isSuccessSaveed;

        }catch (e:Exception){
            return false;

        }

    }
}