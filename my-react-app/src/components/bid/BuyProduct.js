import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { buyProduct } from './bidService'; // buyProduct는 bidService에서 가져옴
import { getProductDetail } from '../product/productService'; // getProductDetail은 productService에서 가져옴
import '../../css/Home.css';
import '../../css/Product.css';

function BuyProduct() {
    const { productId } = useParams();
    const navigate = useNavigate();
    const [product, setProduct] = useState(null);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        getProductDetail(productId)
            .then((response) => setProduct(response.data))
            .catch((error) => console.error('상품 상세 정보 조회 실패:', error));
    }, [productId]);

    const handleBuy = async () => {
        setSubmitting(true);
        try {
            await buyProduct(productId);
            alert('즉시 구매 되었습니다.');
            navigate(`/products/${productId}`);
        } catch (error) {
            const errorMessage = error.response?.data || '구매에 실패했습니다.';
            alert(`구매 실패: ${errorMessage}`);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="app-shell home-container">
            <header className="home-header app-header">
                <button className="logo brand-button" onClick={() => navigate('/products')}>
                    Articket
                </button>
                <nav className="header-actions" aria-label="구매 메뉴">
                    <button className="ghost-button" onClick={() => navigate(`/products/${productId}`)}>상세</button>
                    <button className="ghost-button" onClick={() => navigate('/products')}>목록</button>
                </nav>
            </header>

            <main className="bid-page">
                <section className="bid-summary">
                    <p className="eyebrow">BUY NOW</p>
                    <h1>{product?.productName || '즉시 구매'}</h1>
                    {product && (
                        <dl className="bid-price-grid">
                            <div>
                                <dt>즉시구매가</dt>
                                <dd>{product.buyNowPrice?.toLocaleString()}원</dd>
                            </div>
                            <div>
                                <dt>판매자</dt>
                                <dd>{product.sellerUsername}</dd>
                            </div>
                        </dl>
                    )}
                </section>

                <section className="buy-container">
                    <h2>구매 확정</h2>
                    {product && <p>{product.buyNowPrice?.toLocaleString()}원</p>}
                    <button className="primary-button" onClick={handleBuy} disabled={submitting}>
                        {submitting ? '처리 중' : '즉시구매'}
                    </button>
                    {product && (
                        <Link className="subtle-link" to={`/products/${productId}`}>상품 상세로 돌아가기</Link>
                    )}
                </section>
            </main>
        </div>
    );
}

export default BuyProduct;
