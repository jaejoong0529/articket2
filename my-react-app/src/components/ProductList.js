import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";

function ProductList({ onSelectProduct }) {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = Cookies.get("token");

        if (!token) {
            setError("인증 토큰이 없습니다. 로그인해주세요.");
            setLoading(false);
            return;
        }

        fetch("http://localhost:8080/product/", {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (!response.ok) {
                    if (response.status === 401) {
                        setError("인증에 실패했습니다. 다시 로그인해주세요.");
                        Cookies.remove("token");
                    } else {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                }
                return response.json();
            })
            .then((data) => {
                setProducts(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error fetching products:", err);
                setError(err.message || "상품 목록을 불러오는 중 오류가 발생했습니다.");
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <div style={{ padding: "20px" }}>Loading...</div>;
    }

    if (error) {
        return <div style={{ padding: "20px", color: "red" }}>{error}</div>;
    }

    return (
        <div style={{ padding: "20px" }}>
            <h1>상품 목록</h1>
            <ul>
                {products.map((product) => (
                    <li key={product.id} onClick={() => onSelectProduct(product)}>
                        <h3>{product.productName}</h3>
                        <p>시작가: {product.price} 원</p>
                        <p>즉시 구매가: {product.buyNowPrice} 원</p>
                        {product.image && (
                            <>
                                {console.log(product.image)}
                                <img
                                    src={`http://localhost:8080${product.image}`}
                                    alt={product.productName}
                                    width="150"
                                />
                            </>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ProductList;