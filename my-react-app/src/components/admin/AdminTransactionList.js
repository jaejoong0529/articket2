import React, { useEffect, useState } from 'react';
import '../../css/Admin.css';
import { fetchTransactions, deleteTransaction } from './adminService';
import {useNavigate} from "react-router-dom";

function AdminTransactionList() {
    const [transactions, setTransactions] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadTransactions = async () => {
            try {
                const data = await fetchTransactions();
                setTransactions(data);
            } catch (err) {
                console.error("거래내역 조회 실패:", err);
            }
        };
        loadTransactions();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm("정말로 삭제하시겠습니까?")) {
            try {
                await deleteTransaction(id);
                setTransactions(prev => prev.filter(tx => tx.transactionId !== id));
            } catch (err) {
                console.error("거래 삭제 실패:", err);
            }
        }
    };

    return (
        <div className="admin-container">
            <h2>거래내역 조회</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>상품명</th>
                    <th>구매자</th>
                    <th>판매자</th>
                    <th>가격</th>
                    <th>거래일</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                {transactions.map(tx => (
                    <tr key={tx.transactionId}>
                        <td>{tx.transactionId}</td>
                        <td>{tx.productName}</td>
                        <td>{tx.buyer}</td>
                        <td>{tx.seller}</td>
                        <td>{tx.price}</td>
                        <td>{tx.date}</td>
                        <td>
                            <button onClick={() => handleDelete(tx.transactionId)}>삭제</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={() => navigate(-1)}>뒤로 가기</button>
        </div>
    );
}

export default AdminTransactionList;
