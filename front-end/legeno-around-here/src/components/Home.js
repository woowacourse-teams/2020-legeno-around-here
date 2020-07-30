import React from "react";
import { Link } from "react-router-dom";

function Home() {
  return (
    <div>
      <h2>우리동네 캡짱</h2>
      <Link to="/join">회원가입</Link>
      <br />
      <Link to="/login">로그인</Link>
      <br />
      <Link to="/mypage">마이페이지</Link>
      <br />
    </div>
  );
}

export default Home;
