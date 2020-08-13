import React from 'react';
import styled from 'styled-components';
import IconButton from '@material-ui/core/IconButton';

import submit from '../../images/submit.png';
import SendRoundedIcon from '@material-ui/icons/SendRounded';
// const Submit = styled.button`
//   background: url(${submit}) no-repeat;
//   background-size: 30px;
//   width: 30px;
//   height: 30px;
//   margin-right: 0;
//   margin-left: auto;
//   border: 1px solid green;
//   outline: 0;
//   cursor: pointer;
// `;

const MoveToRight = styled.div`
  margin-left: auto;
`;

const SubmitButton = () => {
  return (
    <MoveToRight>
      <IconButton edge="end">
        <SendRoundedIcon />
      </IconButton>
    </MoveToRight>
  );
};

export default SubmitButton;
