import React from 'react';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import Home from './components/Home';
import Join from './components/Join';
import Login from './components/Login';
import Posting from './components/Posting';
import MyPage from './components/MyPage';
import SectorPage from './components/SectorPage';
import PostDetail from './components/PostDetail';
import HomePage from './components/pages/HomePage';

import './style.css';

function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" exact component={HomePage} />
        <Route path="/join" exact component={Join} />
        <Route path="/login" exact component={Login} />
        <Route path="/mypage" exact component={MyPage} />
        <Route path="/posting" exact component={Posting} />
        <Route path="/sector" exact component={SectorPage} />
        <Route path="/posts/:postId" exact component={PostDetail} />
        <Route path="/home" exact component={HomePage} />
        <Redirect path="*" to="/" />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
