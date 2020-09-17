import React from 'react';
import { Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import SectorManagePage from './pages/SectorManagePage';
import UserManagePage from './pages/UserManagePage';
import QuestionManagePage from './pages/QuestionManagePage';
import PostReportPage from './pages/PostReportPage';
import CommentReportPage from './pages/CommentReportPage';
import SectorReportPage from './pages/SectorReportPage';
import UserReportPage from './pages/UserReportPage';

const App = () => {
  return (
    <>
      <Route path={['/', '/home']} component={HomePage} exact />
      <Route path={['/sectors']} component={SectorManagePage} exact />
      <Route path={['/users']} component={UserManagePage} exact />
      <Route path={['/question']} component={QuestionManagePage} exact />
      <Route path={['/post-report']} component={PostReportPage} exact />
      <Route path={['/comment-report']} component={CommentReportPage} exact />
      <Route path={['/sector-report']} component={SectorReportPage} exact />
      <Route path={['/user-report']} component={UserReportPage} exact />
      <Route path='/login' component={LoginPage} />
    </>
  );
};

export default App;
