// common.js

// 1. 로그인 기능
function login() {
    const id = document.getElementById('userid').value;
    
    if (!id) {
        alert("아이디를 입력해주세요!");
        return;
    }

    // 브라우저에 로그인 정보 저장
    localStorage.setItem('username', id);
    
    alert(id + "님 환영합니다!");
    location.href = "main.html"; // 메인으로 이동
}

// 2. 로그아웃 기능
function logout() {
    localStorage.removeItem('username'); // 저장된 정보 삭제
    alert("로그아웃 되었습니다.");
    location.href = "main.html"; // 메인으로 이동 (새로고침 효과)
}

// 3. ★ [핵심] 페이지 열릴 때 로그인 상태 확인 및 헤더 변경
// (중복된 함수를 하나로 합쳤습니다!)
function checkLogin() {
    const user = localStorage.getItem('username'); // 저장된 아이디 확인
    const barContent = document.querySelector('.bar-content'); // 상단 메뉴 박스 찾기
    
    // 로그인이 되어 있다면? (user 정보가 있다면)
    if (user && barContent) {
        // 내용을 이렇게 바꿔치기 합니다: [OOO님] | [마이페이지] | [로그아웃] | [고객센터]
        barContent.innerHTML = `
            <span style="font-weight:bold; color:#333; margin-right:10px;">${user}님</span>
            <span class="divider">|</span>
            <a href="mypage.html">마이페이지</a>
            <span class="divider">|</span>
            <a href="#" onclick="logout()">로그아웃</a>
            <span class="divider">|</span>
            <a href="#">고객센터</a>
        `;
    }
}

// 페이지가 로드되면 즉시 실행
checkLogin();