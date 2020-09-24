import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Join from './components/pages/JoinPage';
import Login from './components/pages/LoginPage';
import PostingPage from './components/pages/posting/PostingPage';
import SectorPage from './components/pages/sector/SectorPage';
import PostDetailPage from './components/pages/post/PostDetailPage';
import MyProfileEditPage from './components/pages/myProfileEdit/MyProfileEditPage';
import PostingUpdatePage from './components/pages/posting/PostingUpdatePage';
import MyProfilePage from './components/pages/myProfile/MyProfilePage';
import RankingPage from './components/pages/ranking/RankingPage';
import RankingPageReload from './components/pages/ranking/RankingPageReload';
import OthersProfilePage from './components/pages/OthersProfile/OthersProfilePage';
import MyPosts from './components/pages/myProfile/MyPosts';
import HomePage from './components/pages/home/HomePage';
import HomePageReload from './components/pages/home/HomePageReload';
import NotificationPage from './components/pages/home/NotificationPage';
import InitPage from './components/pages/InitPage';
import OtherPosts from './components/pages/OthersProfile/OtherPosts';
import MyAwardPage from './components/pages/myAward/MyAwardPage';
import OtherAwardPage from './components/pages/OtherAward/OtherAwardPage';
import SectorDetailPage from './components/pages/sector/SectorDetailPage';
import WithdrawConfirmPage from './components/pages/myProfile/WithdrawConfirmPage';
import OAuth2RedirectHandler from './components/thirdparty/OAuth2RedirectHandler';
import ErrorPage from './components/pages/ErrorPage';
import FindPasswordPage from './components/pages/find/FindPasswordPage';

function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path='/' exact component={InitPage} />
        <Route path='/home' exact component={HomePage} />
        <Route path='/home-reload' exact component={HomePageReload} />
        <Route path='/join' exact component={Join} />
        <Route path='/login' exact component={Login} />
        <Route path='/find/password' exact component={FindPasswordPage} />
        <Route path='/users/me' exact component={MyProfilePage} />
        <Route path='/posting' exact component={PostingPage} />
        <Route path='/posts/:postId/update' exact component={PostingUpdatePage} />
        <Route path='/sector' exact component={SectorPage} />
        <Route path='/sectors/:sectorId' exact component={SectorDetailPage} />
        <Route path='/posts/:postId' exact component={PostDetailPage} />
        <Route path='/home' exact component={HomePage} />
        <Route path='/ranking' exact component={RankingPage} />
        <Route path='/ranking-reload' exact component={RankingPageReload} />
        <Route path='/myProfileEdit' exact component={MyProfileEditPage} />
        <Route path='/users/:userId' exact component={OthersProfilePage} />
        <Route path='/users/:userId/awards' exact component={OtherAwardPage} />
        <Route path='/my-posts' exact component={MyPosts} />
        <Route path='/my-awards' exact component={MyAwardPage} />
        <Route path='/notification' exact component={NotificationPage} />
        <Route path='/users/:userId/posts' exact component={OtherPosts} />
        <Route path='/confirm-withdraw' exact component={WithdrawConfirmPage} />
        <Route path='/oauth2/redirect' exact component={OAuth2RedirectHandler} />
        <Route path='*' component={ErrorPage} />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
