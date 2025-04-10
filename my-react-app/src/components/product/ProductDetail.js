import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getProductDetail } from "./productService";
import { getHighestBid } from "../bid/bidService"; // getHighestBid 추가

function ProductDetail() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [imageError, setImageError] = useState(false);
    const [highestBid, setHighestBid] = useState(0); // 최고 입찰가 상태 추가

    useEffect(() => {
        const fetchProductDetail = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await getProductDetail(id);
                setProduct(response.data);
                const bidResponse = await getHighestBid(id); // 최고 입찰가 가져오기
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
            <p>현재 최고 입찰가: {formatPrice(highestBid)} 원</p> {/* 최고 입찰가 표시 */}
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
            <div>
                <Link to={`/products/update/${product.id}`}>
                    <button>수정</button>
                </Link>
                <Link to={`/products/delete/${product.id}`}>
                    <button>삭제</button>
                </Link>
                <Link to={`/buy/${product.id}`}>
                    <button>즉시 구매</button>
                </Link>
                <Link to={`/bid/${product.id}`}>
                    <button>입찰하기</button>
                </Link>
            </div>
        </div>
    );
}

export default ProductDetail;