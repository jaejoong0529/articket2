// // Logout.js
// import React from "react";
// import { logout } from "./authService";
//
// function Logout() {
//     const handleLogout = async () => {
//         try {
//             await logout();
//             localStorage.removeItem("token"); // 토큰 삭제
//             localStorage.removeItem("refreshToken"); //리프레시 토큰 삭제
//             console.log("로그아웃 성공");
//         } catch (error) {
//             console.error("Logout failed:", error);
//         }
//     };
//
//     return <button onClick={handleLogout}>로그아웃</button>;
// }
//
// export default Logout;