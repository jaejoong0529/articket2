import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getProductDetail, updateProduct } from './productService';

function ProductUpdate() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [product, setProduct] = useState(null);
    const [productName, setProductName] = useState('');
    const [price, setPrice] = useState('');
    const [buyNowPrice, setBuyNowPrice] = useState('');
    const [description, setDescription] = useState('');
    const [image, setImage] = useState(null);

    useEffect(() => {
        getProductDetail(id).then((response) => {
            const productData = response.data;
            setProduct(productData);
            setProductName(productData.productName);
            setPrice(productData.price.toString());
            setBuyNowPrice(productData.buyNowPrice.toString());
            setDescription(productData.description);
        });
    }, [id]);

    const formatNumber = (value) => {
        const numericValue = value.replace(/[^0-9]/g, '');
        return numericValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    };

    const handlePriceChange = (e) => {
        setPrice(formatNumber(e.target.value));
    };

    const handleBuyNowPriceChange = (e) => {
        setBuyNowPrice(formatNumber(e.target.value));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const priceNumber = parseInt(price.replace(/,/g, ''), 10);
        const buyNowPriceNumber = parseInt(buyNowPrice.replace(/,/g, ''), 10);

        try {
            await updateProduct(id, { request: { productName, price: priceNumber, buyNowPrice: buyNowPriceNumber, description }, image });
            alert('상품이 성공적으로 수정되었습니다.');
            navigate(`/products/${id}`);
        } catch (error) {
            console.error('Error updating product:', error);
            alert('상품 수정에 실패했습니다.');
        }
    };

    if(!product) return <div>loading...</div>

    return (
        <div>
            <h2>상품 수정</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="상품 이름" value={productName} onChange={(e) => setProductName(e.target.value)} />
                <input type="text" placeholder="시작가" value={price} onChange={handlePriceChange} />
                <input type="text" placeholder="즉시 구매가" value={buyNowPrice} onChange={handleBuyNowPriceChange} />
                <textarea placeholder="상품 설명" value={description} onChange={(e) => setDescription(e.target.value)} />
                <input type="file" onChange={(e) => setImage(e.target.files[0])} />
                <button type="submit">수정</button>
            </form>
        </div>
    );
}

export default ProductUpdate;