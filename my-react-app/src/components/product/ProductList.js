import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { getProducts } from "./productService";
import { logout } from "../member/authService";
import queryString from "query-string";
import "../../css/Product.css";
import "../../css/Home.css";

function ProductList() {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
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
    const categoryLabels = {
        "전체": "전체",
        ELECTRONICS: "전자기기",
        FASHION: "패션",
        BOOKS: "도서",
        SPORTS: "스포츠",
        BEAUTY: "뷰티",
    };

    const formatPrice = (price) => {
        if (!price) return "0";
        return price.toLocaleString();
    };

    if (loading) {
        return <div className="app-shell"><div className="loading-message">상품 목록을 불러오는 중입니다...</div></div>;
    }

    if (error) {
        return <div className="app-shell"><div className="error-message">{error}</div></div>;
    }

    return (
        <div className="app-shell home-container">
            <header className="home-header app-header">
                <button className="logo brand-button" onClick={() => navigate('/products')}>
                    Articket
                </button>
                <nav className="header-actions" aria-label="상품 메뉴">
                    <button className="ghost-button" onClick={() => navigate('/recharge')}>충전</button>
                    <button className="ghost-button" onClick={() => navigate('/myinfo')}>내 거래</button>
                    <button className="primary-button" onClick={() => navigate('/products/create')}>상품 등록</button>
                    <button className="text-button" onClick={handleLogout}>로그아웃</button>
                </nav>
            </header>

            <main className="product-page">
                <section className="product-toolbar">
                    <div>
                        <p className="eyebrow">AUCTION MARKET</p>
                        <h1>경매 목록</h1>
                    </div>
                    <div className="search-box">
                        <input
                            type="text"
                            placeholder="상품 이름이나 설명으로 검색"
                            value={keyword}
                            onChange={(e) => setKeyword(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter') handleSearch();
                            }}
                        />
                        <button className="primary-button" onClick={handleSearch}>검색</button>
                    </div>
                </section>

                <div className="category-tabs" role="tablist" aria-label="카테고리">
                    {categories.map((cat) => {
                        const isActive = (cat === "전체" && !category) || category === cat;
                        return (
                            <button
                                key={cat}
                                className={isActive ? "active" : ""}
                                onClick={() => handleCategoryClick(cat)}
                            >
                                {categoryLabels[cat]}
                            </button>
                        );
                    })}
                </div>

                <ul className="product-grid">
                    {products.length === 0 ? (
                        <li className="empty-state">검색 결과가 없습니다.</li>
                    ) : (
                        products.map((product) => (
                            <li key={product.id} className={`product-item ${product.sold ? 'sold' : ''}`}>
                                <div className="product-image-frame">
                                    {product.image ? (
                                        <img
                                            src={`http://localhost:8080${product.image}`}
                                            alt={product.productName}
                                        />
                                    ) : (
                                        <span>{product.productName?.slice(0, 2) || "AR"}</span>
                                    )}
                                </div>
                                <div className="product-card-body">
                                    <div className="product-card-heading">
                                        <h3>{product.productName}</h3>
                                        {product.sold && <span className="sold-label">판매 완료</span>}
                                    </div>
                                    <dl className="price-stack">
                                        <div>
                                            <dt>시작가</dt>
                                            <dd>{formatPrice(product.price)}원</dd>
                                        </div>
                                        <div>
                                            <dt>현재 최고가</dt>
                                            <dd>{product.highestBid ? `${formatPrice(product.highestBid)}원` : "입찰 없음"}</dd>
                                        </div>
                                        <div>
                                            <dt>즉시구매가</dt>
                                            <dd>{formatPrice(product.buyNowPrice)}원</dd>
                                        </div>
                                    </dl>
                                    <Link className="product-link" to={`/products/${product.id}`}>
                                        <button className="primary-button" disabled={product.sold}>상세 보기</button>
                                    </Link>
                                </div>
                            </li>
                        ))
                    )}
                </ul>
            </main>
        </div>
    );
}

export default ProductList;
