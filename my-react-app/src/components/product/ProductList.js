import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { getProducts } from "./productService";
import { getHighestBid } from "../bid/bidService";
import { logout } from "../member/authService";
import queryString from "query-string";
import "../../css/Product.css";
import "../../css/Home.css";

function ProductList() {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [highestBids, setHighestBids] = useState({});
    const [keyword, setKeyword] = useState(""); // 검색어 상태
    const navigate = useNavigate();
    const location = useLocation();

    // URL 쿼리에서 검색어 추출
    const { search: searchKeyword, category } = queryString.parse(location.search);

    useEffect(() => {
        if (searchKeyword) setKeyword(searchKeyword); // 검색창에 반영
        fetchProducts(category, searchKeyword);
    }, [searchKeyword, category]);

    const fetchProducts = (category, search) => {
        setLoading(true);
        getProducts(category, search)
            .then((response) => {
                setProducts(response.data);
                setLoading(false);
                fetchHighestBids(response.data);
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

    const handleSearch = () => {
        const params = {};
        if (keyword.trim()) params.search = keyword.trim();
        if (category) params.category = category;
        navigate(`/products?${queryString.stringify(params)}`);
    };

    const handleCategoryClick = (cat) => {
        const params = {};
        if (cat !== "전체") params.category = cat;
        if (keyword.trim()) params.search = keyword.trim();
        navigate(`/products?${queryString.stringify(params)}`);
    };

    const categories = ["전체", "ELECTRONICS", "FASHION", "BOOKS", "SPORTS", "BEAUTY"];

    if (loading) {
        return <div>상품 목록을 불러오는 중입니다...</div>;
    }

    if (error) {
        return <div style={{ color: "red" }}>{error}</div>;
    }

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>
                    Articket
                </div>
                <div className="button-group">
                    <button onClick={handleLogout}>로그아웃</button>
                    <Link to="/products/create"><button>상품 등록</button></Link>
                    <Link to="/recharge"><button>돈 충전</button></Link>
                    <Link to="/myinfo"><button>내 정보</button></Link>
                </div>
            </header>

            {/* 🔍 검색창 */}
            <div className="search-box">
                <input
                    type="text"
                    placeholder="상품 이름이나 설명으로 검색"
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                />
                <button onClick={handleSearch}>🔍</button>
            </div>

            {/* 카테고리 필터 버튼 */}
            <div className="button-group">
                {categories.map((cat) => (
                    <button key={cat} onClick={() => handleCategoryClick(cat)}>
                        {cat}
                    </button>
                ))}
            </div>

            {/* 상품 리스트 */}
            <ul>
                {products.length === 0 ? (
                    <li>🔍 검색 결과가 없습니다.</li>
                ) : (
                    products.map((product) => (
                        <li key={product.id} className={`product-item ${product.sold ? 'sold' : ''}`}>
                            <div>
                                <h3>{product.productName}</h3>
                                <p>시작가: {product.price} 원</p>
                                <p>즉시 구매가: {product.buyNowPrice} 원</p>
                                <p>현재 최고 입찰가: {highestBids[product.id] || "입찰 없음"} 원</p>
                                {product.sold && <p className="sold-label">✅ 판매 완료</p>}
                            </div>
                            {product.image && (
                                <img
                                    src={`http://localhost:8080${product.image}`}
                                    alt={product.productName}
                                />
                            )}
                            <Link to={`/products/${product.id}`}>
                                <button disabled={product.sold}>상세 정보</button>
                            </Link>
                        </li>
                    ))
                )}
            </ul>
        </div>
    );
}

export default ProductList;
