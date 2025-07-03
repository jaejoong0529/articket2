import React, { useEffect, useState } from 'react';
import '../../css/Admin.css';
import { fetchMembers, deleteMember } from './adminService';
import {useNavigate} from "react-router-dom"; // 공통 CSS 파일 import


function AdminMemberList() {
    const [members, setMembers] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();


    useEffect(() => {
        const loadMembers = async () => {
            try {
                const data = await fetchMembers();
                setMembers(data);
            } catch (error) {
                console.error("회원 조회 실패:", error);
            } finally {
                setLoading(false);
            }
        };

        loadMembers();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm("정말로 삭제하시겠습니까?")) {
            try {
                await deleteMember(id);
                setMembers(prev => prev.filter(member => member.memberId !== id));
            } catch (error) {
                const errorMessage = error.response?.data || '삭제에 실패했습니다.';
                alert(`삭제 실패: ${errorMessage}`);
            }
        }
    };

    if (loading) return <div>회원 정보를 불러오는 중입니다...</div>;

    return (
        <div className="admin-container">
            <h2>회원 관리</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>이메일</th>
                    <th>닉네임</th>
                    <th>아이디</th>
                    <th>역할</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                {members.map(member => (
                    <tr key={member.memberId}>
                        <td>{member.memberId}</td>
                        <td>{member.email}</td>
                        <td>{member.nickname}</td>
                        <td>{member.username}</td>
                        <td>{member.role}</td>
                        <td>
                            <button onClick={() => handleDelete(member.memberId)}>삭제</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={() => navigate(-1)}>뒤로 가기</button>
        </div>
    );
}

export default AdminMemberList;
