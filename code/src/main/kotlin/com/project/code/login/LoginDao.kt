package com.project.code.login

import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.project.code.dataClass.dataClassSet
import com.project.code.entity.Account
import com.project.code.entity.UserData
import com.project.code.repository.AccountRepository
import com.project.code.repository.UserDataRepository
import java.util.*


/**
* ----------------------------------------------------------------------------------
* 2023.07.07 최현우
*
* 디비 repository를 사용하려면 클래스의 성생자를 통해 AccountRepository를 받아와야 주입이
* 되는 문제때문에 생성자 인자값 추가
* ----------------------------------------------------------------------------------
* 2023.07.10 최현우
*
* Dao에서  @Autowired으로 사용 가능하여서 해당 생성자에서 객체 의존성 주입하는부분 수정
* ----------------------------------------------------------------------------------
*/
@Component
class LoginDao() {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userDataRepository: UserDataRepository

    fun GetAccountData(userId : String, userPw : String): Optional<Account> {

        //디비에서 키값(요양기관번호 같은거 가져오는 로직) 현재는 SEQ가 키
        //var accountData = accountRepository.findById(1);

        var accountData = accountRepository.findByAccIdAndAccPw(userId, userPw);

        return accountData
    }

    /**
     * ----------------------------------------------------------------------------------
     * 2023.07.12 최현우
     *
     * 로그인 시 유저 정보를 리턴하는 메서드.
     * Account 정보와 유저정보를 따로 관리하기 위하여 분리 저장하도록 함.
     * ----------------------------------------------------------------------------------
     */
    fun Register(registerInfo : dataClassSet.RegisterInfo) : Boolean{

        var userPwd = registerInfo.password

        try {

            userPwd =  DigestUtils.sha512Hex(registerInfo.password)

            //Account Info 저장
            accountRepository.save(Account(registerInfo.id, userPwd, registerInfo.accLoginDate, "5", "USER",registerInfo.accStDate, registerInfo.accEnDate, registerInfo.email, registerInfo.accPwChangeDate))
            //UserData 저장
            userDataRepository.save(UserData(registerInfo.email, registerInfo.cellphone, registerInfo.name, registerInfo.nickname, registerInfo.work))

            return true

        }catch (e:Exception) {
            e.printStackTrace()
            return false

        }

    }

    fun GetUserData(userSeq : Long): Optional<UserData> {

        //디비에서 키값(요양기관번호 같은거 가져오는 로직) 현재는 SEQ가 키
        //var accountData = accountRepository.findById(1);
        var userData = userDataRepository.findById(userSeq)

        return userData

    }



}