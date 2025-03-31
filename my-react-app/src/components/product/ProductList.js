import React, { useEffect, useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { getProducts } from "./productService";
import { logout } from "../member/authService";

function ProductList() {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        getProducts()
            .then((response) => {
                setProducts(response.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error fetching products:", err);
                if (err.response && err.response.status === 401) {
                    setError("인증에 실패했습니다. 다시 로그인해주세요.");
                    localStorage.removeItem("accessToken");
                    localStorage.removeItem("refreshToken");
                } else {
                    setError(err.message || "상품 목록을 불러오는 중 오류가 발생했습니다.");
                }
                setLoading(false);
            });
    }, []);

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    if (loading) {
        return <div style={{ padding: "20px" }}>상품 목록을 불러오는 중입니다...</div>;
    }

    if (error) {
        return <div style={{ padding: "20px", color: "red" }}>{error}</div>;
    }

    return (
        <div style={{ padding: "20px" }}>
            <h1>상품 목록</h1>
            <button onClick={handleLogout}>로그아웃</button>
            <Link to="/products/create">
                <button>상품 등록</button>
            </Link>
            <Link to="/recharge"> {/* 돈 충전 페이지로 이동하는 버튼 추가 */}
                <button>돈 충전</button>
            </Link>
            <ul>
                {products.map((product) => (
                    <li key={product.id}>
                        <h3>{product.productName}</h3>
                        <p>시작가: {product.price} 원</p>
                        <p>즉시 구매가: {product.buyNowPrice} 원</p>
                        {product.image && (
                            <img
                                src={`http://localhost:8080${product.image}`}
                                alt={product.productName}
                                width="150"
                            />
                        )}
                        <Link to={`/products/${product.id}`}>
                            <button>상세 정보</button>
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ProductList;