import React, { useState } from "react";
import Cookies from "js-cookie"; // js-cookie 라이브러리 import

function CreateProduct({ onProductCreated }) {
    const [productName, setProductName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [buyNowPrice, setBuyNowPrice] = useState("");
    const [image, setImage] = useState(null);
    const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지 상태 추가

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage(""); // 에러 메시지 초기화
        const token = Cookies.get("token"); // 쿠키에서 토큰 가져오기

        if (!token) {
            setErrorMessage("인증 토큰이 없습니다. 로그인해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append(
            "request",
            new Blob([JSON.stringify({ productName, description, price, buyNowPrice })], {
                type: "application/json",
            })
        );
        if (image) formData.append("image", image);

        try {
            const response = await fetch("http://localhost:8080/product/create", {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`, // 인증 헤더 추가
                },
                body: formData,
            });

            if (response.ok) {
                alert("상품이 등록되었습니다!");
                onProductCreated(); // 목록 새로고침
            } else {
                const errorData = await response.json(); // 서버에서 보낸 에러 메시지 파싱
                setErrorMessage(errorData.message || "등록 실패"); // 에러 메시지 설정
            }
        } catch (error) {
            setErrorMessage("상품 등록 중 오류가 발생했습니다."); // 네트워크 오류 처리
            console.error("상품 등록 오류:", error);
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h2>상품 등록</h2>
            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>} {/* 에러 메시지 표시 */}
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="상품명"
                    value={productName}
                    onChange={(e) => setProductName(e.target.value)}
                    required
                />
                <input
                    type="text"
                    placeholder="설명"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
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
                <input type="file" onChange={(e) => setImage(e.target.files[0])} />
                <button type="submit">등록</button>
            </form>
        </div>
    );
}

export default CreateProduct;