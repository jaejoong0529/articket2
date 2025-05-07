import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { deleteProduct } from './productService';
import '../../css/Home.css';
import '../../css/Product.css';

function ProductDelete() {
    const { id } = useParams();
    const navigate = useNavigate();

    const handleDelete = async () => {
        try {
            await deleteProduct(id);
            alert('상품이 성공적으로 삭제되었습니다.');
            navigate('/products');
        } catch (error) {
            console.error('Error deleting product:', error);
            alert('상품 삭제에 실패했습니다.');
        }
    };

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>
                    Articket
                </div>
            </header>
            <div className="delete-confirmation">
                <h2>상품 삭제</h2>
                <p>정말 이 상품을 삭제하시겠습니까?</p>
                <div className="button-group">
                    <button onClick={handleDelete}>삭제</button>
                    <button onClick={() => navigate(-1)}>뒤로 가기</button>
                </div>
            </div>
        </div>
    );
}

export default ProductDelete;
