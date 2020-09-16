import React from 'react';
import Token from '../components/validation/Token';
import Dashboard from '../components/Dashboard';

const MainPage = ({ history }) => {

  return (
    <>
      <Token history={history}/>
      <Dashboard/>
    </>
  );
};

export default MainPage;
