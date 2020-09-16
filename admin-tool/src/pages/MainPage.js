import React from 'react';
import Token from '../components/validation/Token';
import Home from '../components/Home';

const MainPage = ({ history }) => {

  return (
    <>
      <Token history={history}/>
      <Home/>
    </>
  );
};

export default MainPage;
