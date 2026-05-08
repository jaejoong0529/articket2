import React, { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { getProductDetail } from "./productService";
import { getHighestBid } from "../bid/bidService";
import '../../css/Home.css'; // Import the general home styles
import '../../css/ProductDetail.css'; // Import the specific product detail styles

function ProductDetail() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [imageError, setImageError] = useState(false);
    const [highestBid, setHighestBid] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProductDetail = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await getProductDetail(id);
                setProduct(response.data);
                const bidResponse = await getHighestBid(id);
                setHighestBid(bidResponse);
            } catch (err) {
                console.error("Error fetching product detail:", err);
                setError("상품 상세 정보를 불러오는데 실패했습니다. 다시 시도해주세요.");
            } finally {
                setLoading(false);
            }
        };
        fetchProductDetail();
    }, [id]);

    const formatPrice = (price) => {
        if (!price) return "0";
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    };

    const formatDate = (date) => {
        if (!date) return "-";
        return new Date(date).toLocaleString();
    };

    if (loading) {
        return <div className="loading-message">상품 정보를 로딩 중입니다...</div>;
    }

    if (error) {
        return (
            <div className="error-message">
                {error}
                <button onClick={() => window.location.reload()}>다시 시도</button>
            </div>
        );
    }

    if (!product) {
        return <div style={{ padding: "20px" }}>상품을 찾을 수 없습니다.</div>;
    }

    return (
        <div className="app-shell home-container">
            <header className="home-header app-header">
                <button className="logo brand-button" onClick={() => navigate('/products')}>
                    Articket
                </button>
                <nav className="header-actions" aria-label="상세 메뉴">
                    <button className="ghost-button" onClick={() => navigate('/products')}>목록</button>
                    <button className="ghost-button" onClick={() => navigate('/myinfo')}>내 거래</button>
                </nav>
            </header>

            <main className="detail-page">
                <section className="detail-media">
                    {product.image && !imageError ? (
                        <img
                            src={`http://localhost:8080${product.image}`}
                            alt={product.productName}
                            onError={() => setImageError(true)}
                        />
                    ) : (
                        <div className="detail-placeholder">{product.productName?.slice(0, 2) || "AR"}</div>
                    )}
                </section>

                <section className="detail-panel">
                    <p className="eyebrow">상품 #{product.id}</p>
                    <h1>{product.productName}</h1>
                    <p className="detail-description">{product.description}</p>

                    <dl className="detail-prices">
                        <div>
                            <dt>현재 최고가</dt>
                            <dd>{formatPrice(highestBid)}원</dd>
                        </div>
                        <div>
                            <dt>즉시구매가</dt>
                            <dd>{formatPrice(product.buyNowPrice)}원</dd>
                        </div>
                        <div>
                            <dt>시작가</dt>
                            <dd>{formatPrice(product.price)}원</dd>
                        </div>
                    </dl>

                    <dl className="detail-meta">
                        <div>
                            <dt>판매자</dt>
                            <dd>{product.sellerUsername}</dd>
                        </div>
                        <div>
                            <dt>등록일</dt>
                            <dd>{formatDate(product.createdAt)}</dd>
                        </div>
                        <div>
                            <dt>종료일</dt>
                            <dd>{formatDate(product.endTime)}</dd>
                        </div>
                    </dl>

                    <div className="detail-actions">
                        <Link to={`/bid/${product.id}`}><button className="primary-button">입찰하기</button></Link>
                        <Link to={`/buy/${product.id}`}><button className="secondary-button">즉시구매</button></Link>
                        <Link to={`/products/update/${product.id}`}><button className="ghost-button">수정</button></Link>
                        <Link to={`/products/delete/${product.id}`}><button className="danger-button">삭제</button></Link>
                    </div>
                </section>
            </main>
        </div>
    );
}

export default ProductDetail;
