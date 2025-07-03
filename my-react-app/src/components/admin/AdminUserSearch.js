// AdminUserSearch.jsx
import React, { useState } from 'react';
import '../../css/Admin.css';
import { searchMembers } from './adminService';
import {useNavigate} from "react-router-dom";

function AdminUserSearch() {
    const [keyword, setKeyword] = useState('');
    const [results, setResults] = useState([]);
    const navigate = useNavigate();

    const handleSearch = async () => {
        try {
            const data = await searchMembers(keyword);
            setResults(data);
        } catch (error) {
            alert('회원 검색 실패');
        }
    };

    return (
        <div className="admin-container">
            <h2>회원 검색</h2>
            <input
                type="text"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                placeholder="아이디, 닉네임 또는 이메일"
            />
            <button onClick={handleSearch}>검색</button>
            <ul>
                {results.map((member) => (
                    <li key={member.id}>
                        ID: {member.username}, 닉네임: {member.nickname}, 이메일: {member.email}
                    </li>
                ))}
            </ul>
            <button onClick={() => navigate(-1)}>뒤로 가기</button>
        </div>
    );
}

export default AdminUserSearch;
