import React, { useEffect, useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { getProducts } from "./productService";
import { getHighestBid } from "../bid/bidService"; // getHighestBid 추가
import { logout } from "../member/authService";
import '../../css/Product.css'; // 공통 CSS 파일 import
import '../../css/Home.css'; // 공통 CSS 파일 import


function ProductList() {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const [highestBids, setHighestBids] = useState({}); // 최고 입찰가 상태 추가

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = (category) => {
        setLoading(true);
        getProducts(category)
            .then((response) => {
                setProducts(response.data);
                setLoading(false);
                fetchHighestBids(response.data); // 최고 입찰가 가져오기
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
    };

    const fetchHighestBids = async (products) => {
        const bids = {};
        for (const product of products) {
            try {
                const bid = await getHighestBid(product.id);
                bids[product.id] = bid;
            } catch (error) {
                console.error(`Error fetching highest bid for product ${product.id}:`, error);
            }
        }
        setHighestBids(bids);
    };

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    if (loading) {
        return <div>상품 목록을 불러오는 중입니다...</div>;
    }

    if (error) {
        return <div style={{ color: "red" }}>{error}</div>;
    }

    const categories = ["전체", "ELECTRONICS", "FASHION", "BOOKS", "SPORTS", "BEAUTY"];

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>
                    Articket
                </div>
                <div className="button-group">
                    <button onClick={handleLogout}>로그아웃</button>
                    <Link to="/products/create">
                        <button>상품 등록</button>
                    </Link>
                    <Link to="/recharge">
                        <button>돈 충전</button>
                    </Link>
                    <Link to="/myinfo">
                        <button>내 정보</button>
                    </Link>
                </div>
            </header>
            <div className="button-group">
                {categories.map((category) => (
                    <button key={category} onClick={() => fetchProducts(category === "전체" ? null : category)}>
                        {category}
                    </button>
                ))}
            </div>
            <ul>
            {products.map((product) => (
                    <li key={product.id} className="product-item">
                        <div>
                            <h3>{product.productName}</h3>
                            <p>시작가: {product.price} 원</p>
                            <p>즉시 구매가: {product.buyNowPrice} 원</p>
                            <p>현재 최고 입찰가: {highestBids[product.id] || "입찰 없음"} 원</p>
                        </div>
                        {product.image && (
                            <img
                                src={`http://localhost:8080${product.image}`}
                                alt={product.productName}
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
