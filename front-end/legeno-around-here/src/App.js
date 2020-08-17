import React from 'react';
import { BrowserRouter, Redirect, Route, Switch } from 'react-router-dom';
import Join from './components/pages/Join';
import Login from './components/pages/Login';
import PostingPage from './components/pages/posting/PostingPage';
import MyProfile from './components/pages/MyProfile';
import SectorPage from './components/pages/sector/SectorPage';
import PostDetailPage from './components/pages/post/PostDetailPage';
import Home from './components/pages/Home';
import Ranking from './components/pages/Ranking';

function App() {
  const mainArea = localStorage.getItem('mainAreaName');

  if (!mainArea) {
    localStorage.setItem('mainAreaName', '서울특별시');
    localStorage.setItem('mainAreaId', 1);
  }

  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" exact component={Home} />
        <Route path="/join" exact component={Join} />
        <Route path="/login" exact component={Login} />
        <Route path="/myProfile" exact component={MyProfile} />
        <Route path="/posting" exact component={PostingPage} />
        <Route path="/sector" exact component={SectorPage} />
        <Route path="/posts/:postId" exact component={PostDetailPage} />
        <Route path="/home" exact component={Home} />
        <Route path="/ranking" exact component={Ranking} />
        <Redirect path="*" to="/" />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
