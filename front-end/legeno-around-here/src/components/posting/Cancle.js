import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

import cancle from '../../images/cancle.png';

const CancleButton = styled.button`
  background: url(${cancle}) no-repeat;
  background-size: 30px;
  width: 30px;
  height: 30px;
  margin-left: 8px;
  margin-right: auto;
  border: 0;
  outline: 0;
  cursor: pointer;
`;

const CancleLink = styled(Link)`
  margin-right: auto;
`;

const Cancle = ({ linkTo }) => {
  return (
    <CancleLink to={ linkTo }>
      <CancleButton></CancleButton>
    </CancleLink>
  );
};

export default Cancle;
