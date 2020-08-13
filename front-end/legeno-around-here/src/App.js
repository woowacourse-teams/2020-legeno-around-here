import React from 'react';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import Join from './components/pages/Join';
import Login from './components/pages/Login';
import Posting from './components/pages/Posting';
import MyPage from './components/pages/MyPage';
import Sector from './components/pages/Sector';
import PostDetail from './components/pages/PostDetail';
import Home from './components/pages/Home';

import './style.css';
import Ranking from './components/pages/Ranking';

function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" exact component={Home} />
        <Route path="/join" exact component={Join} />
        <Route path="/login" exact component={Login} />
        <Route path="/mypage" exact component={MyPage} />
        <Route path="/posting" exact component={Posting} />
        <Route path="/sector" exact component={Sector} />
        <Route path="/posts/:postId" exact component={PostDetail} />
        <Route path="/home" exact component={Home} />
        <Route path="/ranking" exact component={Ranking} />
        <Redirect path="*" to="/" />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
