import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createProduct } from "./productService";
import '../../css/Home.css'; // Home 스타일 사용
import '../../css/CreateProduct.css'; // 공통 CSS 파일 import


function CreateProduct() {
    const [productName, setProductName] = useState("");
    const [price, setPrice] = useState("");
    const [buyNowPrice, setBuyNowPrice] = useState("");
    const [description, setDescription] = useState("");
    const [image, setImage] = useState(null);
    const [categoryId, setCategoryId] = useState("ELECTRONICS");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createProduct({
                request: {
                    productName,
                    price,
                    buyNowPrice,
                    description,
                    category: categoryId,
                },
                image,
            });
            alert("상품이 성공적으로 등록되었습니다.");
            navigate("/products");
        } catch (error) {
            const errorMessage = error.response?.data || '상품 등록 실패했습니다.';
            alert(`등록 실패: ${errorMessage}`);
        }
    };

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>Articket</div>
            </header>

            <main className="main-content">
                <h1 className="main-title">📝 상품 등록</h1>
                <form onSubmit={handleSubmit} className="center-form">

                    <input
                        type="text"
                        placeholder="상품 이름"
                        value={productName}
                        onChange={(e) => setProductName(e.target.value)}
                        required
                    />
                    <input
                        type="number"
                        placeholder="시작가"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                    <input
                        type="number"
                        placeholder="즉시 구매가"
                        value={buyNowPrice}
                        onChange={(e) => setBuyNowPrice(e.target.value)}
                        required
                    />
                    <textarea
                        placeholder="상품 설명"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                        rows={4}
                    />
                    <input
                        type="file"
                        onChange={(e) => setImage(e.target.files[0])}
                    />
                    <select
                        value={categoryId}
                        onChange={(e) => setCategoryId(e.target.value)}
                    >
                        <option value="ELECTRONICS">ELECTRONICS</option>
                        <option value="FASHION">FASHION</option>
                        <option value="BOOKS">BOOKS</option>
                        <option value="SPORTS">SPORTS</option>
                        <option value="BEAUTY">BEAUTY</option>
                    </select>
                    <div className="button-group">
                        <button type="submit">등록</button>
                        <button type="button" onClick={() => navigate(-1)}>← 뒤로가기</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default CreateProduct;
