package com.project.code.entity;

import jakarta.persistence.*;



/**
* --------------------------------------------------
* 2023.07.07 최현우
*
* 로그인 시 DB에서 가져오는 유저 정보
* JPA를 사용하므로 Entity구성.
* 계정 등록 시에도 사용됨.
* 확장자를 java로 사용한 이유는 kt를 사용하여 코틀린 문법을
* 사용할 경우 JPA 규칙에 위배되는 사항이 존재하여 자바 클래스 생성
* --------------------------------------------------
*/
@Entity
@Table(name = "UserInf")
public class UserData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserSeq", length = 20, nullable = false)
    private Long userSeq;
    public UserData(){}

    public UserData(String email, String cellPhone, String name, String nickName, String work) {
        this.email = email;
        this.cellPhone = cellPhone;
        this.name = name;
        this.nickName = nickName;
        this.work = work;
    }

    @Column(name = "Email", length = 30)
    private String email;

    @Column(name = "CellPhone",length = 14)
    private String cellPhone;

    @Column(name = "Name", length = 10)
    private String name;

    @Column(name = "NicName", length = 30)
    private String nickName;

    @Column(name = "Work", length = 50)
    private String work;


    public String getEmail() {
        return email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public String getWork() {
        return work;
    }

}
