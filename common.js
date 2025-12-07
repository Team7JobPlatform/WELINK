// // common.js

// // 1. 로그인 기능
// function login() {
//     const id = document.getElementById('userid').value;
    
//     if (!id) {
//         alert("아이디를 입력해주세요!");
//         return;
//     }

//     // 브라우저에 로그인 정보 저장
//     localStorage.setItem('username', id);
    
//     alert(id + "님 환영합니다!");
//     location.href = "main.html"; // 메인으로 이동
// }

// // 2. 로그아웃 기능
// function logout() {
//     localStorage.removeItem('username'); // 저장된 정보 삭제
//     alert("로그아웃 되었습니다.");
//     location.href = "main.html"; // 메인으로 이동 (새로고침 효과)
// }

// // 3. ★ [핵심] 페이지 열릴 때 로그인 상태 확인 및 헤더 변경
// // (중복된 함수를 하나로 합쳤습니다!)
// function checkLogin() {
//     const user = localStorage.getItem('username'); // 저장된 아이디 확인
//     const barContent = document.querySelector('.bar-content'); // 상단 메뉴 박스 찾기
    
//     // 로그인이 되어 있다면? (user 정보가 있다면)
//     if (user && barContent) {
//         // 내용을 이렇게 바꿔치기 합니다: [OOO님] | [마이페이지] | [로그아웃] | [고객센터]
//         barContent.innerHTML = `
//             <span style="font-weight:bold; color:#333; margin-right:10px;">${user}님</span>
//             <span class="divider">|</span>
//             <a href="mypage.html">마이페이지</a>
//             <span class="divider">|</span>
//             <a href="#" onclick="logout()">로그아웃</a>
//             <span class="divider">|</span>
//             <a href="#">고객센터</a>
//         `;
//     }
// }

// // 페이지가 로드되면 즉시 실행
// checkLogin();
// common.js

document.addEventListener('DOMContentLoaded', () => {
    updateHeaderStatus(); // 모든 페이지가 로드되면 헤더 상태부터 확인
});

// ★ 헤더 상태 업데이트 함수 (로그인 O -> 환영합니다 / 로그인 X -> 로그인 버튼)
function updateHeaderStatus() {
    const token = localStorage.getItem('accessToken');
    const userName = localStorage.getItem('userName');
    
    // 헤더의 링크 영역 ID (main.html, mypage.html 모두 이 ID가 있어야 함)
    const authLinksContainer = document.getElementById('header-auth-links');

    if (!authLinksContainer) return; // 헤더가 없는 페이지면 패스

    if (token && userName) {
        // [로그인 된 상태]
        authLinksContainer.innerHTML = `
            <span style="color: #333; font-weight: 700; margin-right: 10px;">
                환영합니다, ${userName}님!
            </span>
            <span style="color: #ddd;">|</span>
            <a href="mypage.html" style="font-weight: bold; margin: 0 10px;">마이페이지</a>
            <span style="color: #ddd;">|</span>
            <a href="#" onclick="logout(); return false;" style="margin: 0 10px;">로그아웃</a>
            <span style="color: #ddd;">|</span>
            <a href="#" style="margin-left: 10px;">고객센터</a>
        `;
    } else {
        // [로그인 안 된 상태 - 로그아웃 후 이 상태로 돌아옴]
        authLinksContainer.innerHTML = `
            <a href="login.html">로그인</a>
            <span class="divider">|</span>
            <a href="index.html">회원가입</a>
            <span class="divider">|</span>
            <a href="#">고객센터</a>
        `;
    }
}

// ★ 로그아웃 함수
function logout() {
    // 1. 브라우저 메모리 싹 비우기
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userName');
    localStorage.removeItem('userId');
    
    alert("성공적으로 로그아웃 되었습니다.");
    
    // 2. 메인 페이지로 이동 (새로고침 효과)
    window.location.href = 'main.html';
}