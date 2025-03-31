import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createProduct } from "./productService";

function CreateProduct() {
    const [productName, setProductName] = useState("");
    const [price, setPrice] = useState("");
    const [buyNowPrice, setBuyNowPrice] = useState("");
    const [description, setDescription] = useState("");
    const [image, setImage] = useState(null);
    const [categoryId, setCategoryId] = useState("ELECTRONICS"); // 기본 카테고리 설정
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createProduct({
                request: { productName, price, buyNowPrice, description, category: categoryId }, // categoryId를 category로 변경
                image,
            });
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
                <select value={categoryId} onChange={(e) => setCategoryId(e.target.value)}>
                    <option value="ELECTRONICS">ELECTRONICS</option>
                    <option value="FASHION">FASHION</option>
                    <option value="BOOKS">BOOKS</option>
                    <option value="SPORTS">SPORTS</option>
                    <option value="BEAUTY">BEAUTY</option>
                </select>
                <button type="submit">등록</button>
            </form>
        </div>
    );
}

export default CreateProduct;