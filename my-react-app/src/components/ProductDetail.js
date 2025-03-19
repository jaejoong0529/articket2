import React from "react";

function ProductDetail({ product, onBack }) {
    if (!product) return <p>상품을 선택해주세요.</p>;

    return (
        <div style={{ padding: "20px" }}>
            <button onClick={onBack}>뒤로가기</button>
            <h2>{product.productName}</h2>
            <p>{product.description}</p>
            <p>시작가: {product.price} 원</p>
            <p>즉시 구매가: {product.buyNowPrice} 원</p>
            {product.image && (
                <img
                    src={`http://localhost:8080/uploads/${product.image}`}
                    alt={product.productName}
                    width="200"
                />
            )}
        </div>
    );
}

export default ProductDetail;
