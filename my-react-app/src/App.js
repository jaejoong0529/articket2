import React, { useState, useEffect } from "react";

import ProductList from "./components/ProductList";

import ProductDetail from "./components/ProductDetail";

import CreateProduct from "./components/CreateProduct";

import Login from "./components/Login";

import Signup from "./components/Signup";

import RefreshToken from "./components/RefreshToken";

import Cookies from "js-cookie";



function App() {

  const [selectedProduct, setSelectedProduct] = useState(null);

  const [showCreateForm, setShowCreateForm] = useState(false);

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const [showLogin, setShowLogin] = useState(false);

  const [showSignup, setShowSignup] = useState(false);



  useEffect(() => {

    const token = Cookies.get("token");

    if (token) {

      setIsLoggedIn(true);

    }

  }, []);



  const handleLoginSuccess = () => {

    setIsLoggedIn(true);

    setShowLogin(false);

  };



  return (

      <div>

        <h1>경매 플랫폼</h1>



        {showLogin && <Login onLoginSuccess={handleLoginSuccess} />}

        {showSignup && <Signup />}



        {!isLoggedIn ? (

            <>

              <button onClick={() => setShowLogin(true)}>로그인</button>

              <button onClick={() => setShowSignup(true)}>회원가입</button>

            </>

        ) : (

            <>

              <RefreshToken />

              {selectedProduct ? (

                  <ProductDetail product={selectedProduct} onBack={() => setSelectedProduct(null)} />

              ) : showCreateForm ? (

                  <CreateProduct onProductCreated={() => setShowCreateForm(false)} />

              ) : (

                  <>

                    <button onClick={() => setShowCreateForm(true)}>상품 등록</button>

                    <ProductList onSelectProduct={setSelectedProduct} />

                  </>

              )}

            </>

        )}

      </div>

  );

}



export default App;