import React, { useEffect, useState } from 'react';
import '../../css/Admin.css';
import { fetchProducts, deleteProduct } from './adminService';
import {useNavigate} from "react-router-dom"; // 공통 CSS 파일 import


function AdminProductList() {
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadProducts = async () => {
            try {
                const data = await fetchProducts();
                setProducts(data);
            } catch (err) {
                console.error("상품 조회 실패:", err);
            }
        };
        loadProducts();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm("정말로 삭제하시겠습니까?")) {
            try {
                await deleteProduct(id);
                setProducts(prev => prev.filter(product => product.productId !== id));
            } catch (err) {
                console.error("상품 삭제 실패:", err);
            }
        }
    };

    return (
        <div className="admin-container">
            <h2>상품/경매 관리</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>상품명</th>
                    <th>판매자</th>
                    <th>시작가</th>
                    <th>즉시구매가</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                {products.map(product => (
                    <tr key={product.productId}>
                        <td>{product.productId}</td>
                        <td>{product.productName}</td>
                        <td>{product.sellerUsername}</td>
                        <td>{product.price}</td>
                        <td>{product.buyNowPrice}</td>
                        <td>
                            <button onClick={() => handleDelete(product.productId)}>삭제</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={() => navigate(-1)}>뒤로 가기</button>
        </div>
    );
}

export default AdminProductList;
