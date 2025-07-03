import React, { useState } from 'react';
import '../../css/Admin.css';
import { filterProducts } from './adminService';
import {useNavigate} from "react-router-dom"; // 공통 CSS 파일 import


function AdminProductFilter() {
    const [isSold, setIsSold] = useState(false);
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();
    const handleFilter = async () => {
        try {
            const data = await filterProducts(isSold);
            setProducts(data);
        } catch (error) {
            alert('상품 필터링 실패');
        }
    };

    return (
        <div className="admin-container">
            <h2>상품 필터링</h2>
            <label>
                <input
                    type="checkbox"
                    checked={isSold}
                    onChange={(e) => setIsSold(e.target.checked)}
                />
                판매 완료된 상품 보기
            </label>
            <button onClick={handleFilter}>필터 적용</button>

            <ul>
                {products.map((product) => (
                    <li key={product.productId}>
                        상품명: {product.productName} / 가격: {product.price.toLocaleString()}원
                    </li>
                ))}
            </ul>
            <button onClick={() => navigate(-1)}>뒤로 가기</button>
        </div>
    );
}

export default AdminProductFilter;
