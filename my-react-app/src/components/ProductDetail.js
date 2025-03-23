import React, { useState, useEffect } from "react";
import axios from "axios";
import Cookies from "js-cookie";

const ProductDetail = ({ product, onBack }) => {
    const [bidAmount, setBidAmount] = useState("");
    const [highestBid, setHighestBid] = useState(0);
    const token = Cookies.get("token");

    useEffect(() => {
        axios.get(`http://localhost:8080/bid/${product.id}`, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => setHighestBid(response.data))
            .catch(error => console.error("Error fetching highest bid", error));
    }, [product.id, token]);

    const handleBid = async () => {
        try {
            await axios.post(
                "http://localhost:8080/bid/bidProduct",
                { productId: product.id, bidAmount: parseInt(bidAmount, 10) },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            alert("입찰 성공!");
            setBidAmount("");
        } catch (error) {
            alert(error.response?.data || "입찰 실패");
        }
    };

    const handleBuyNow = async () => {
        try {
            await axios.post(
                "http://localhost:8080/bid/buyProduct",
                { productId: product.id, buyAmount: product.buyNowPrice },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            alert("즉시 구매 완료!");
            onBack();
        } catch (error) {
            alert(error.response?.data || "구매 실패");
        }
    };

    return (
        <div>
            <h2>{product.name}</h2>
            <p>현재 최고 입찰가: {highestBid}원</p>
            <p>즉시 구매가: {product.buyNowPrice}원</p>
            <img
                src={`http://localhost:8080${product.image}`}
                alt={product.productName}
                width="150"
            />
            <input
                type="number"
                value={bidAmount}
                onChange={(e) => setBidAmount(e.target.value)}
                placeholder="입찰 금액 입력"
            />
            <button onClick={handleBid}>입찰하기</button>
            <button onClick={handleBuyNow}>즉시 구매</button>
            <button onClick={onBack}>뒤로가기</button>
        </div>
    );
};

export default ProductDetail;
