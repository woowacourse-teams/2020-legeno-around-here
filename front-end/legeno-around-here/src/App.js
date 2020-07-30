import React from "react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";
import Home from "./components/Home";
import JoinForm from "./components/user/JoinForm";
import LoginForm from "./components/user/LoginForm";
import MyPage from "./components/user/MyPage";

import "./style.css";

function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" exact component={Home} />
        <Route path="/join" exact component={JoinForm} />
        <Route path="/login" exact component={LoginForm} />
        <Route path="/mypage" exact component={MyPage} />
        <Redirect path="*" to="/" />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
