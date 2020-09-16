import React from 'react';
import Token from '../components/validation/Token';

const MainPage = ({ history }) => {

  return (
    <>
      <Token history={history}/>
      <div>메인 페이지</div>
    </>
  );
};

export default MainPage;
