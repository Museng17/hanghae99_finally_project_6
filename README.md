# <img src = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcxZbxk%2FbtrIpGR7AFS%2FHRidH7Kcig9hVI4uIkDVP1%2Fimg.png" align=left width=50>모음:moum

<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FXcWUT%2FbtrIktNepat%2FIoEWZqOKGkg4UkKGEWsXP0%2Fimg.png" width="100%">

### 채팅방이나 메모함에 지저분하게 쌓여있는 정보들이 있나요?

- 모음(moum) : 모음은 나의 정보 아카이브 서비스 입니다.

- 메모와 링크를 간편하게 분류하고 저장할 수 있어요

🗂 **[moum 서비스 바로가기](https://moum.cloud/)**

🎞 **[서비스 시연 영상](https://youtu.be/iqAPE-S5U0Y)**


<br />

## 💡 기획의도
`"👩 채팅방에 쌓아둔 정보를 다시 찾아 보기 힘들어요."`

`"👩‍🦱 급하게 붙여넣은 정보를, 나중에 필요할 때 다시 찾아 쓸 수 있다면 편리하지 않을까요?"`

`"👱‍♂️ 내가 모아둔 정보들을 깔끔하게 정리해 둘 곳이 필요해요!"`

**모음은 이런 생각에서 기획하게 된 서비스입니다.**

<br />

## ✨ 핵심기능

<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fb2tCUu%2FbtrIoCbxN2z%2FnkySFWvB68tp8qbbjeUfwK%2Fimg.png" width="80%" >

<br />

### 💻 로그인 - [Wiki 보러 가기](https://github.com/Mmuseng/hanghae99_finally_project_6/wiki/%EA%B8%B0%EB%8A%A5-:-%EB%A1%9C%EA%B7%B8%EC%9D%B8)
- JWT토큰을 활용한 로그인 기능 구현
- jwt 리플레쉬토큰을 활용해 엑세스토큰이 만료되었을 때 재발급 기능 구현
- HandlerInterceptor를 이용한 토큰체크 기능 구현

<br />

### 💻 소셜로그인(구글) - [Wiki 보러 가기](https://github.com/Mmuseng/hanghae99_finally_project_6/wiki/%EA%B8%B0%EB%8A%A5-:-%EC%86%8C%EC%85%9C%EB%A1%9C%EA%B7%B8%EC%9D%B8)
- 구글 로그인 구현
- 구글의 회원정보를 받아 회원가입시키는 로직으로 구현
- 소셜로그인된 회원의 아이디로 리프레쉬토큰과 엑세스토큰을 발급

<br />

### 💻 이메일 인증을 활용한 회원가입 - [Wiki 보러가기](https://github.com/Mmuseng/hanghae99_finally_project_6/wiki/%EA%B8%B0%EB%8A%A5-:--%EC%9D%B8%EC%A6%9D%EB%B2%88%ED%98%B8%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%9D%B8%EC%A6%9D)
- 이메일에 인증번호를 전송해 인증하는 방식으로 인증구현
- 비밀번호를 잊어버렸을 때 임시비밀번호를 이메일로 전송 하도록 구현

<br />

### 💻 조각(게시물), 모음(폴더) CRUD - [Wiki 보러가기](https://github.com/Mmuseng/hanghae99_finally_project_6/wiki/%EA%B8%B0%EB%8A%A5-:-CRUD)
- 조각, 모음 검색 및 필터(최신순, 사용자 지정순) 적용
- 조회시 Pageable를 통해 페이징 적용
- 조각, 모음 정렬 순서를 사용자가 직접 순서를 정렬할 수 있도록 적용
- 조각에 해당되는 카테고리만 카테고리 목록에 나올 수 있도록 적용

<br />

### 💻 Jmeter부하테스트 진행 후 성능개선  - [Wiki 보러가기](https://github.com/Mmuseng/hanghae99_finally_project_6/wiki/%EB%B6%80%ED%95%98-%ED%85%8C%EC%8A%A4%ED%8A%B8--%EB%B0%8F-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0#%EF%B8%8F-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0-%EC%82%AC%ED%95%AD)
- Jmeter 부하테스트를 통해 평균속도, 에러율, 시간당 처리량, 평균바이트 체크
- N+1문제 해결(쿼리 최적화) 및 필요없는 이중 반복문 및 Stream으로 단순 반복문사용 제거 
- API 속도 총 2366ms 개선 및 에러율 2% 까지 낮추는 성과를 냄




<br />
 
 
 ## 📆 프로젝트 기간
* 2022년 06월 24일 ~ 2022년 08월 05일   
* 배포 : 2022년 07월 27일

<br />

## 👥 백엔드 팀원 소개


#### `Backend (3명)`

 + 김민주 (부팀장)

 + 서다빈

 + 백현명
 
 <br />

## 🧩 서비스 아키텍쳐
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbXnNXe%2FbtrIUmSXjaO%2FBAMHu0qHP6Y6VI0gSwLBBK%2Fimg.png">

<br />

## ⚙ ERD 설계
<details>
<summary>ERD</summary>
<div markdown="1">
<img src="https://user-images.githubusercontent.com/81284265/181692536-d9085c23-b86f-4621-b3e2-864f1e772161.png" width="1000px">
</div>
</details>

<br />


## 📂 노션  
📔  **[노션 링크](https://neat-apartment-b02.notion.site/moum-625b66e189ee4151b21f2f60e8935582)**

<br />

 
**Backend Tech Stack**  
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> 
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=white">
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"> 
<img src="https://img.shields.io/badge/codedeploy-6DB33F?style=for-the-badge&logo=codedeploy&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
<img src="https://img.shields.io/badge/Apache JMeter-D22128?style=for-the-badge&logo=Apache JMeter&logoColor=white">
<img src="https://img.shields.io/badge/Git-00000?style=for-the-badge&logo=Git&logoColor=F05032]"/>
<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white]"/>
