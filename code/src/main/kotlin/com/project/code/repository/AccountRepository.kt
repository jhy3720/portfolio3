package com.project.code.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import com.project.code.entity.Account
import java.util.*

/**
* --------------------------------------------------
* 2023.07.07 최현우
*
* DB JPA에 사용되는 레파지토리, 아래에 선언된 함수는 정해진
* 규칙대로 사용해야 정상작동함.
* JpaRepository<Account, Long> 는 첫번째 인자는 리턴될
* 즉 DB스키마를 표현한 클래스 이고, 두번째 인자는 기본키의 자료형.
* --------------------------------------------------
*/
@Repository
interface AccountRepository : JpaRepository<Account, Long> {

    fun findByAccIdAndAccPw(AccId : String , AccPw : String) : Optional<Account>

}