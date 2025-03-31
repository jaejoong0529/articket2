import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { deleteProduct } from './productService';

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
        <div>
            <h2>상품 삭제</h2>
            <p>정말 이 상품을 삭제하시겠습니까?</p>
            <button onClick={handleDelete}>삭제</button>
            <button onClick={() => navigate(`/products/${id}`)}>취소</button>
        </div>
    );
}

export default ProductDelete;