import React from 'react';
import styled from 'styled-components';

import cancle from '../images/cancle.png';
import submit from '../images/submit.png';

const Cancle = styled.div`
  background: url(${cancle}) no-repeat;
  background-size: 30px;
  width: 30px;
  height: 30px;
  margin-left: 8px;
  margin-right: auto;
`;

const Submit = styled.div`
  background: url(${submit}) no-repeat;
  background-size: 30px;
  width: 30px;
  height: 30px;
  margin-right: 8px;
  margin-left: auto;
`;

const TopBarBackground = styled.div`
  background: #eeeeee;
  color: black;
  height: 4rem;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const PostingTopBar = () => {
  return (
    <TopBarBackground>
      <Cancle/>
      <div>성북구</div>
      <Submit/>
    </TopBarBackground>
  );
};

export default PostingTopBar;
