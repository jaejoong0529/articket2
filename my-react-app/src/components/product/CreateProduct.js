// CreateProduct.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createProduct } from "./productService";

function CreateProduct() {
    const [productName, setProductName] = useState("");
    const [price, setPrice] = useState("");
    const [buyNowPrice, setBuyNowPrice] = useState("");
    const [description, setDescription] = useState("");
    const [image, setImage] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createProduct({ request: { productName, price, buyNowPrice, description }, image });
            alert("상품이 성공적으로 등록되었습니다.");
            navigate("/products");
        } catch (error) {
            console.error("상품 등록 실패:", error);
            alert("상품 등록에 실패했습니다.");
        }
    };

    return (
        <div>
            <h2>상품 등록</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="상품 이름" value={productName} onChange={(e) => setProductName(e.target.value)} required />
                <input type="number" placeholder="시작가" value={price} onChange={(e) => setPrice(e.target.value)} required />
                <input type="number" placeholder="즉시 구매가" value={buyNowPrice} onChange={(e) => setBuyNowPrice(e.target.value)} required />
                <textarea placeholder="상품 설명" value={description} onChange={(e) => setDescription(e.target.value)} required />
                <input type="file" onChange={(e) => setImage(e.target.files[0])} />
                <button type="submit">등록</button>
            </form>
        </div>
    );
}

export default CreateProduct;