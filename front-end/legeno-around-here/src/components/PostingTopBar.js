import React from 'react';
import styled from 'styled-components';

import cancle from '../images/cancle.png';
import submit from '../images/submit.png';
import { Link } from 'react-router-dom';

const CancleButton = styled.button`
  background: url(${cancle}) no-repeat;
  background-size: 30px;
  width: 30px;
  height: 30px;
  margin-left: 8px;
  margin-right: auto;
  border: 0;
  outline: 0;
`;

const CancleLink = styled(Link)`
    margin-right: auto;
`;

export const Cancle = ({ linkTo }) => {
  return (
    <CancleLink to={ linkTo }>
      <CancleButton></CancleButton>
    </CancleLink>
  );
};

export const Submit = styled.button`
  background: url(${submit}) no-repeat;
  background-size: 30px;
  width: 30px;
  height: 30px;
  margin-right: 8px;
  margin-left: auto;
  border: 0;
  outline: 0;
`;

export const TopBarBackground = styled.div`
  background: #dddddd;
  color: black;
  height: 4rem;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
`;
