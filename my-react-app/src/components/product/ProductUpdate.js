import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { getProductDetail, updateProduct } from './productService';
import '../../css/Home.css';
import '../../css/Product.css';

function ProductUpdate() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [product, setProduct] = useState(null);
    const [productName, setProductName] = useState('');
    const [price, setPrice] = useState('');
    const [buyNowPrice, setBuyNowPrice] = useState('');
    const [description, setDescription] = useState('');
    const [image, setImage] = useState(null);
    const [category, setCategory] = useState('');

    useEffect(() => {
        getProductDetail(id).then((response) => {
            const productData = response.data;
            setProduct(productData);
            setProductName(productData.productName);
            setPrice(formatNumber(productData.price.toString()));
            setBuyNowPrice(formatNumber(productData.buyNowPrice.toString()));
            setDescription(productData.description);
            setCategory(productData.category); //  기존 카테고리 값 세팅
        });
    }, [id]);

    const formatNumber = (value) => {
        const numericValue = value.replace(/[^0-9]/g, '');
        return numericValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    };

    const handlePriceChange = (e) => {
        setPrice(e.target.value);
    };

    const handleBuyNowPriceChange = (e) => {
        setBuyNowPrice(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const priceNumber = parseInt(price.replace(/,/g, ''), 10);
        const buyNowPriceNumber = parseInt(buyNowPrice.replace(/,/g, ''), 10);

        try {
            await updateProduct(id, {
                request: {
                    productName,
                    price: priceNumber,
                    buyNowPrice: buyNowPriceNumber,
                    description,
                    category, //  포함
                },
                image,
            });
            alert('상품이 성공적으로 수정되었습니다.');
            navigate(`/products/${id}`);
        } catch (error) {
            const errorMessage = error.response?.data || '수정에 실패했습니다.';
            alert(`수정 실패: ${errorMessage}`);
        }
    };

    if (!product) return <div className="loading-message">상품 정보를 불러오는 중...</div>;

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>
                    Articket
                </div>
            </header>

            <div className="product-update-container">
                <h2>상품 수정</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="상품 이름"
                        value={productName}
                        onChange={(e) => setProductName(e.target.value)}
                        required
                    />
                    <input
                        type="text"
                        placeholder="시작가"
                        value={price}
                        onChange={handlePriceChange}
                        required
                    />
                    <input
                        type="text"
                        placeholder="즉시 구매가"
                        value={buyNowPrice}
                        onChange={handleBuyNowPriceChange}
                        required
                    />
                    <textarea
                        placeholder="상품 설명"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                    <select value={category} onChange={(e) => setCategory(e.target.value)} required>
                        <option value="">카테고리 선택</option>
                        <option value="ELECTRONICS">전자제품</option>
                        <option value="FASHION">의류</option>
                        <option value="BOOKS">책</option>
                        <option value="SPORTS">스포츠</option>
                        <option value="BEAUTY">뷰티</option>
                    </select>
                    <input
                        type="file"
                        onChange={(e) => setImage(e.target.files[0])}
                        accept="image/*"
                    />
                    <button type="submit">수정</button>
                </form>

                <div className="button-group">
                    <button onClick={() => navigate(-1)}>← 뒤로 가기</button>
                    <Link to={`/products/${id}`}>
                        <button>상품 상세로 이동</button>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default ProductUpdate;
