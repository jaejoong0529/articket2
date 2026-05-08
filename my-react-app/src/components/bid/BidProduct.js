import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { bidProduct, getHighestBid } from './bidService';
import { getProductDetail } from '../product/productService'; //  추가
import '../../css/Home.css';
import '../../css/Bid.css';

function BidProduct() {
    const { productId } = useParams();
    const [bidAmount, setBidAmount] = useState('');
    const [highestBid, setHighestBid] = useState(0);
    const [product, setProduct] = useState(null); //  상품 정보 상태
    const [submitting, setSubmitting] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAll = async () => {
            try {
                const productRes = await getProductDetail(productId);
                setProduct(productRes.data);

                const highestBidValue = await getHighestBid(productId);
                if (typeof highestBidValue === 'number' && !isNaN(highestBidValue)) {
                    setHighestBid(highestBidValue);
                } else {
                    console.error("최고 입찰가 데이터가 유효하지 않음");
                    alert("최고 입찰가를 불러오는 데 실패했습니다.");
                }
            } catch (error) {
                console.error("데이터 조회 실패:", error);
                alert("상품 또는 입찰 정보를 불러오는 데 실패했습니다.");
            }
        };

        fetchAll();
    }, [productId]);

    const formatPrice = (price) => {
        return price != null ? price.toLocaleString() : '정보 없음';
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            await bidProduct(productId, parseInt(bidAmount, 10));
            alert('입찰이 완료되었습니다.');
            navigate(`/products/${productId}`);
        } catch (error) {
            const errorMessage = error.response?.data || '입찰에 실패했습니다.';
            alert(`입찰 실패: ${errorMessage}`);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="app-shell home-container">
            <header className="home-header app-header">
                <button className="logo brand-button" onClick={() => navigate('/products')}>Articket</button>
                <nav className="header-actions" aria-label="입찰 메뉴">
                    <button className="ghost-button" onClick={() => navigate(`/products/${productId}`)}>상세</button>
                    <button className="ghost-button" onClick={() => navigate('/products')}>목록</button>
                </nav>
            </header>

            <main className="bid-page">
                <section className="bid-summary">
                    <p className="eyebrow">BID</p>
                    <h1>{product?.productName || '상품 입찰'}</h1>
                    {product && (
                        <dl className="bid-price-grid">
                            <div>
                                <dt>시작가</dt>
                                <dd>{formatPrice(product.price)}원</dd>
                            </div>
                            <div>
                                <dt>현재 최고가</dt>
                                <dd>{highestBid === 0 ? '입찰 없음' : `${formatPrice(highestBid)}원`}</dd>
                            </div>
                        </dl>
                    )}
                </section>

                <section className="bid-container">
                    <h2>입찰 금액</h2>
                    <form onSubmit={handleSubmit}>
                        <input
                            type="number"
                            min={highestBid || product?.price || 1}
                            placeholder="입찰 금액"
                            value={bidAmount}
                            onChange={(e) => setBidAmount(e.target.value)}
                            required
                        />
                        <button className="primary-button" type="submit" disabled={submitting}>
                            {submitting ? '처리 중' : '입찰하기'}
                        </button>
                    </form>
                    <Link className="subtle-link" to={`/products/${productId}`}>상품 상세로 돌아가기</Link>
                </section>
            </main>
        </div>
    );
}

export default BidProduct;
