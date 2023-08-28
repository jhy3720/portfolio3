package com.project.code.entity;


import jakarta.persistence.*;


/**
* --------------------------------------------------
* 2023.07.07 최현우
*
* 로그인 시 DB에서 가져오는 계정 정보
* JPA를 사용하므로 Entity구성.(기존 dataclass)
* --------------------------------------------------
* 2023.07.12 최현우
*
* 로그인 시 리턴되는 값을 계정정보와 유저정보로 나누기 위하여
* 해당 클래스 컬럼을 조금변경
* --------------------------------------------------
*/
@Entity
@Table(name = "AccInf")
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccSeq", nullable = false)
    private Long accSeq;

    @Column(name = "AccId", length = 20, nullable = false)
    private String accId;

    @Column(name = "AccPw", length = 200, nullable = false)
    private String accPw;

    @Column(name = "AccLoginDate", length = 14)
    private String accLoginDate;

    @Column(name = "AccLoginCount", length = 2)
    private String accLoginCount;

    public Account(){}
    public Account(String accId, String accPw, String accLoginDate, String accLoginCount, String accRole, String accStDate, String accEndDate, String accEmail, String accPwChangeDate) {
        this.accId = accId;
        this.accPw = accPw;
        this.accLoginDate = accLoginDate;
        this.accLoginCount = accLoginCount;
        this.accRole = accRole;
        this.accStDate = accStDate;
        this.accEndDate = accEndDate;
        this.accEmail = accEmail;
        this.accPwChangeDate = accPwChangeDate;
    }

    @Column(name = "AccRole", length = 8)
    private String accRole;

    @Column(name = "AccStDate", length = 14)
    private String accStDate;

    @Column(name = "AccEndDate", length = 14)
    private String accEndDate;

    @Column(name = "AccEmail", length = 40)
    private String accEmail;

    @Column(name = "AccPwChangeDate", length = 14)
    private String accPwChangeDate;

    // Getters and Setters


    public String getAccId() {
        return accId;
    }

    public String getAccLoginDate() {
        return accLoginDate;
    }

    public String getAccLoginCount() {
        return accLoginCount;
    }

    public String getAccRole() {
        return accRole;
    }

    public String getAccStDate() {
        return accStDate;
    }

    public String getAccEndDate() {
        return accEndDate;
    }

    public String getAccEmail() {
        return accEmail;
    }

    public String getAccPwChangeDate() {
        return accPwChangeDate;
    }


    //2023.07.07 getKey로 함수명을 생성하면 Account 를 프론트로 리턴할 때 getter로 착각하여 Key값을 프론트로 보내는데.
    //key는 JWT토큰에 생성되는 데이터이므로 front로 보낼필요는 없다고 판단하여 메서드로 전환함.
    public String GetAssembleAccKey() {
        return String.valueOf(accSeq + accId+ accPw);
    }

    public Long GetSeqNum() {
        return accSeq;
    }


}

