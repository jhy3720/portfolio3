//main.tsx
import React, { useState, useRef, useEffect } from 'react';
import axios from '../lib/Axios'; // Axios 인스턴스 가져오기
import { Cookies } from 'react-cookie';
import moment from 'moment';
import 'moment/locale/ko';	//대한민국
import '../css/Main.css';
import { Route, Link, useNavigate, Routes } from "react-router-dom";
import ReactDOM from 'react-dom';
import { Await } from 'react-router-dom';


/**
*------------------------------------------------------------------------
* 2023.08.21 유재호
*
* 쿠키편집 기능 함수
*------------------------------------------------------------------------
*/
const cookies = new Cookies();

export const setCookie = (name: string, value: string, options?: any) => {
 	return cookies.set(name, value, {...options}); 
}

export const getCookie = (name: string) => {
 return cookies.get(name); 
}

export const removeCookie = (name: string) => {
  return cookies.remove(name);
};

function Main() {
  //페이지 이동을 위한 함수 반환
  const navigate = useNavigate();

  //2023.08.21 유재호 PageFlipped 변수 저장
  const [pageFlipped, setPageFlipped] = useState(false);

  //2023.08.21 유재호 pageLogin 변수 저장
  const [pageLogin, setpageLogin] = useState(false);

  //2023.08.21 유재호 IP 변수 저장
  const [ip, setIp] = useState('');

  //2023.08.21 유재호 Id 변수 저장
  let [id, setId] = useState("");

  //2023.08.21 유재호 Password 변수 저장
  let [pwd, setPwd] = useState("");

  //2023.08.23 유재호 전화번호 변수 저장
  let [phone, setPhone] = useState("");

  //2023.08.23 유재호 이메일 변수 저장
  let [email, setEmail] = useState("");

  //2023.08.23 유재호 이름 변수 저장
  let [name, setName] = useState("");

  //2023.08.23 유재호 nickname 변수 저장
  let [nickname, setNickname] = useState("");


  //2023.08.21 유재호 프로젝트 실행 시 최초 IP주소를 불러와 저장
  useEffect(() => {
    fetch('https://ipapi.co/json/')
      .then((response) => response.json())
      .then((data) => setIp(data.ip))
      .catch((error) => console.error(error));
  }, []); // 빈 배열을 전달하면 컴포넌트가 마운트되기 직전에만 이 훅이 실행됩니다.


  /**
  *------------------------------------------------------------------------
  *  
  * 2023.08.23 유재호
  * 
  * 개인정보 변수 입력 시 호출하여 값 입력
  *------------------------------------------------------------------------
  */
  const ChangeId = (event: React.ChangeEvent<HTMLInputElement>) => {

    id = event.target.value;
  };

  const ChangePw = (event: React.ChangeEvent<HTMLInputElement>) => {

    pwd = event.target.value;
  };

  const ChangeName = (event: React.ChangeEvent<HTMLInputElement>) => {

    name = event.target.value;
  };

  const ChangeEmail = (event: React.ChangeEvent<HTMLInputElement>) => {

    email = event.target.value;
  };

  const ChangePhone = (event: React.ChangeEvent<HTMLInputElement>) => {

    phone = event.target.value;
  };

  const ChangeNick = (event: React.ChangeEvent<HTMLInputElement>) => {

    nickname = event.target.value;
  };
  
  /**
  *------------------------------------------------------------------------
  *  
  * 2023.08.23 유재호
  * 
  * 이메일 유효성 검사
  *------------------------------------------------------------------------
  */
  const checkEmail = (email :string) => {
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i

    // 형식에 맞는 경우 true 리턴
    return regExp.test(email)
  }

  /**
  *------------------------------------------------------------------------
  *  
  * 2023.08.23 유재호
  * 
  * 이메일 유효성 검사와 회원가입 정보를 생성하는 메소드
  *------------------------------------------------------------------------
  */
  const Register = async()=>{

    try {

      let isTrue = await checkEmail(email);

      const nowTime = moment().format('YYYYMMDDHHmmss');

      if(isTrue){

        const response = await axios.post('/restapis/login/register', {

          id: id,
          password: pwd,
          cellphone: phone,
          email:email,
          name:name,
          nickname:nickname,
          accLoginDate:nowTime,
          accStDate:nowTime,
          accEndDate:"99991231235959",
          accPwChangeDate:nowTime

        });

        console.log(response) 
        //리턴 값 처리

      }else{
       
        window.alert("이메일 형식을 확인하세요")
      }


    } catch (error) {
      // 예외 처리
    }

  }

  /**
  *------------------------------------------------------------------------
  *  
  * 2023.08.21 유재호
  * 
  * 계정정보를 보내어 토큰값을 리턴받는 메서드
  * 리턴값으로 받은 토큰 및 키 값을 로컬 스토리지에 저장
  *------------------------------------------------------------------------
  */
  const Login = async () => {

    try {

      if (!ip) {
        alert('IP 주소를 불러오는 중입니다. 잠시 후 다시 시도해주세요.');
        return;
      }

      await axios.post('/restapis/login/loginToken', {
        userId: id, // 프론트엔드에서 입력한 사용자 ID
        userPw: pwd, // 프론트엔드에서 입력한 비밀번호
        userIP: ip, // 프론트엔드에서 입력한 IP 주소
        //userKey: "key", //키값 요양병원 번호같은?        
      }).then(response =>{

        // 로컬 스토리지에 토큰 저장
        localStorage.setItem("authToken", response.data.TOKEN); // response.data.token
        localStorage.setItem("key", response.data.ACCOUNTDATA.accountKey);
        console.log(response);
        //쿠키 저장
        setCookie("Islogin",response.data.TOKEN.substring(0,9)+moment().format('YYYYMMDDHHmmss')); 

      }).catch(error =>{

        alert("ID 혹은 비밀번호가 일치하지않습니다.")// 예외 처리
        
      })

    } catch (error) {     

      console.log(error);

    }

  };

  /**
  *------------------------------------------------------------------------
  * 2023.08.21 유재호
  *
  * setPageFlipped 함수를 반대값으로 변경
  *------------------------------------------------------------------------
  */

  /*
  const PageState = () => {

    setPageFlipped(!pageFlipped);

  };
  */


  /**
  *------------------------------------------------------------------------
  * 2023.08.21 유재호
  *
  * pageFilped를 true로 변경 후 page1의 스타일 변경하는 함수
  *------------------------------------------------------------------------
  */
  const MenuClick = () => {

    setPageFlipped(true); 

    const page1 = document.getElementById('page1');

    if (page1) {

      page1.style.transform = 'rotateY(-180deg)';
      page1.style.opacity = '0';

    }

  };

  /**
  *------------------------------------------------------------------------
  * 2023.08.21 유재호
  *
  * page 변수를 true로 변경 후 page1,2의 스타일 변경하는 함수
  *------------------------------------------------------------------------
  */
  const LoginClick = () => {

    setpageLogin(true); 

    const page1 = document.getElementById('page1');
    const page2 = document.getElementById('page2')

    if (page1) {

      page1.style.transform = 'rotateY(-180deg)';
      page1.style.opacity = '0';

    }

    if (page2){

      page2.style.transform = 'rotateY(-180deg)';
      page2.style.opacity = '0';

    }

  };

  /**
  *------------------------------------------------------------------------
  * 2023.08.23 유재호
  *
  * page 변수를 true로 변경 후 page3의 스타일 변경하는 함수
  *------------------------------------------------------------------------
  */

  const RegClick = () => {

    setPageFlipped(true); 

    const page3 = document.getElementById('page3');

    if (page3) {

      page3.style.transform = 'rotateY(-180deg)';
      page3.style.opacity = '0';

    }

    /*
    setTimeout(() => navigate("/Register"), 900);
    */
  };

  /**
  *------------------------------------------------------------------------
  * 2023.08.21 유재호
  *
  * page 변수를 false로 변경하여 모든 페이지의 변화 초기화하는 함수
  *------------------------------------------------------------------------
  */
  const HomeClick = () => {

    setPageFlipped(false); 

    const page1 = document.getElementById('page1');
    const page2 = document.getElementById('page2');
    const page3 = document.getElementById('page3');
    const page4 = document.getElementById('page4');

    if (page1) {
      page1.style.transform = 'none'; // 회전 효과 초기화
      page1.style.opacity = '1'; // 투명도 초기화
    }

    if (page2) {
      page2.style.transform = 'none';
      page2.style.opacity = '1'; 
    }


    if (page3) {
      page3.style.transform = 'none';
      page3.style.opacity = '1'; 
    }

    if (page4) {
      page4.style.transform = 'none';
      page4.style.opacity = '1'; 
    }

  };




  return (

    <div className="main">

      {/*<div className={`book ${pageFlipped ? 'flipped' : ''}`} onClick={PageState}>*/}

      <div className="book"> 

        <section id="pageSection">

           {/*메인 화면*/}
          <div className="page" id="page1">

            <button id='login' type="button" onClick={LoginClick}>

              로그인

            </button>

            <h1>이 맛 어때</h1>
            <p>Welcome</p>

            <button id='next' type="button" onClick={MenuClick}>

              메뉴 보기

            </button>

          </div>

          {/*주문 화면*/}
          <div className="page" id="page2">

            <button id='home' type="button" onClick={HomeClick}>

            처음으로

            </button>

            <h2>메뉴</h2>
            <input type="text" defaultValue={id} onChange={(event)=>ChangeId(event)} name="메뉴" placeholder="메뉴" id="menu" />

            <h2>위치</h2>
            <input type="text" defaultValue={pwd} onChange={(event)=>ChangePw(event)}  placeholder="장소" id='local'/>


            <button id='result' type="button" onClick={MenuClick}>

            주문하기

            </button>

          </div>

          {/*로그인 화면*/}
          <div className="page" id="page3">

            <button id='home' type="button" onClick={HomeClick}>

            처음으로

            </button>

            <button id='btnreg' type="button" onClick={RegClick}>

            회원가입

            </button>

            <h2>아이디</h2>
            <label htmlFor="ID">ID</label>
            <input type="text" defaultValue={id} onChange={(event)=>ChangeId(event)} name="아이디" placeholder="아이디" id="ID" />


            <h2>비밀번호</h2>
            <label htmlFor="password">password</label>
            <input type="password" defaultValue={pwd} onChange={(event)=>ChangePw(event)}  placeholder="비밀번호" id='password'/>

            <button id='btnlogin' type="button" onClick={Login}>

            로그인

            </button>


          </div>

          {/* 가입 화면 */}
          <div className="page" id="page4">

            <button id='home' type="button" onClick={HomeClick}>

            처음으로

            </button>

            <div className="inf">

            <label htmlFor="ID">ID</label>
            <input type="text" name="아이디" placeholder="아이디" id="RegID" />

            <label htmlFor="pwd">비밀번호</label>
            <input type="password" name="비밀번호" placeholder="비밀번호" id="Regpassword" />

            <label htmlFor="name">이름</label>
            <input type="text" name="이름" placeholder="이름" id="name" />

            <label htmlFor="email">이메일</label>
            <input type="text" name="이메일" placeholder="이메일" id="email" />

            <label htmlFor="phone">핸드폰번호</label>
            <input type="text" name="핸드폰번호" placeholder="핸드폰번호" id="phone" />

            <label htmlFor="nickname">닉네임</label>
            <input type="text" name="닉네임" placeholder="닉네임" id="nickname" />

            <button onClick={Register}>회원가입</button>

            </div>
          
          </div>

        </section>

      </div>

    </div>

  );
};

export default Main;
