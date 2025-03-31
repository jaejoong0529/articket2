import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProductDetail } from "./productService";

function ProductDetail() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [imageError, setImageError] = useState(false);

    useEffect(() => {
        const fetchProductDetail = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await getProductDetail(id);
                setProduct(response.data);
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

    if (loading) {
        return <div style={{ padding: "20px" }}>상품 정보를 로딩 중입니다...</div>;
    }

    if (error) {
        return (
            <div style={{ padding: "20px", color: "red" }}>
                {error}
                <button onClick={() => window.location.reload()}>다시 시도</button>
            </div>
        );
    }

    if (!product) {
        return <div style={{ padding: "20px" }}>상품을 찾을 수 없습니다.</div>;
    }

    return (
        <div style={{ padding: "20px" }}>
            <h1>{product.productName}</h1>
            <p>상품 ID: {product.id}</p>
            <p>판매자: {product.sellerUsername}</p>
            <p>시작가: {formatPrice(product.price)} 원</p>
            <p>즉시 구매가: {formatPrice(product.buyNowPrice)} 원</p>
            <p>등록일: {product.createdAt}</p>
            <p>종료일: {product.endTime}</p>
            {product.image && (
                <img
                    src={`http://localhost:8080${product.image}`}
                    alt={product.productName}
                    width="300"
                    onError={() => setImageError(true)}
                />
            )}
            {imageError && <p>이미지를 불러오는데 실패했습니다.</p>}
            <p>{product.description}</p>
        </div>
    );
}

export default ProductDetail;