import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Signup from './components/member/Signup';
import Login from './components/member/Login';
import Logout from './components/member/Logout';
import FindUsername from './components/member/FindUsername';
import FindPassword from './components/member/FindPassword';
import ChangePassword from './components/member/ChangePassword';
import RechargeMoney from './components/member/RechargeMoney';
import RefreshTokenComponent from './components/member/RefreshTokenComponent';
import Home from './components/Home';
import ProductList from './components/product/ProductList';
import ProductDetail from './components/product/ProductDetail';
import CreateProduct from "./components/product/CreateProduct";
import ProductUpdate from './components/product/ProductUpdate';
import ProductDelete from './components/product/ProductDelete';
import BidProduct from './components/bid/BidProduct';
import HighestBid from './components/bid/HighestBid';
import UserBids from './components/bid/UserBids';
import BuyProduct from './components/bid/BuyProduct';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/login" element={<Login />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/findUsername" element={<FindUsername />} />
                <Route path="/findPassword" element={<FindPassword />} />
                <Route path="/changePassword" element={<ChangePassword />} />
                <Route path="/recharge" element={<RechargeMoney />} />
                <Route path="/refresh" element={<RefreshTokenComponent />} />
                <Route path="/products" element={<ProductList />} />
                <Route path="/products/:id" element={<ProductDetail />} />
                <Route path="/products/create" element={<CreateProduct />} />
                <Route path="/products/update/:id" element={<ProductUpdate />} />
                <Route path="/products/delete/:id" element={<ProductDelete />} />
                <Route path="/bid/:productId" element={<BidProduct />} />
                <Route path="/highestBid/:productId" element={<HighestBid />} />
                <Route path="/userBids/:memberId" element={<UserBids />} />
                <Route path="/buy/:productId" element={<BuyProduct />} />
            </Routes>
        </Router>
    );
}

export default App;