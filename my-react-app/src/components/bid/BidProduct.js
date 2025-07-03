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
        try {
            await bidProduct(productId, parseInt(bidAmount, 10));
            alert('입찰이 완료되었습니다.');
            navigate(`/products/${productId}`);
        } catch (error) {
            const errorMessage = error.response?.data || '입찰에 실패했습니다.';
            alert(`입찰 실패: ${errorMessage}`);
        }
    };

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>Articket</div>
            </header>

            <div className="bid-container">
                <h2>상품 입찰</h2>

                {product && (
                    <p>
                        {highestBid === 0
                            ? `현재 입찰가가 없습니다. (시작가: ${formatPrice(product.price)}원)`
                            : `현재 최고 입찰가: ${formatPrice(highestBid)}원`}
                    </p>
                )}

                <form onSubmit={handleSubmit}>
                    <input
                        type="number"
                        placeholder="입찰 금액"
                        value={bidAmount}
                        onChange={(e) => setBidAmount(e.target.value)}
                    />
                    <button type="submit">입찰</button>
                </form>
            </div>

            <div className="button-group">
                <button onClick={() => navigate(-1)}>뒤로 가기</button>
                <Link to={`/products/${productId}`}>
                    <button>상세 정보</button>
                </Link>
            </div>
        </div>
    );
}

export default BidProduct;
