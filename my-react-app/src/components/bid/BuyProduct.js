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

    useEffect(() => {
        getProductDetail(productId)
            .then((response) => setProduct(response.data))
            .catch((error) => console.error('상품 상세 정보 조회 실패:', error));
    }, [productId]);

    const handleBuy = async () => {
        try {
            await buyProduct(productId);
            alert('즉시 구매 되었습니다.');
            navigate(`/products/${productId}`);
        } catch (error) {
            const errorMessage = error.response?.data || '구매에 실패했습니다.';
            alert(`구매 실패: ${errorMessage}`);
        }
    };

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>
                    Articket
                </div>
            </header>
            <div className="buy-container">
                <h2>즉시 구매</h2>
                {product && <p>즉시 구매 가격: {product.buyNowPrice}원</p>}
                <button onClick={handleBuy}>구매</button>
            </div>
            <div className="button-group">
                <button onClick={() => navigate(-1)}>뒤로 가기</button>
                {product && (
                    <Link to={`/products/${productId}`}>
                        <button>상세 정보</button>
                    </Link>
                )}
            </div>
        </div>
    );
}

export default BuyProduct;
