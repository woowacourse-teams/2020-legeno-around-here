import React from 'react';
import styled from 'styled-components';
import IconButton from '@material-ui/core/IconButton';
import SendRoundedIcon from '@material-ui/icons/SendRounded';

const MoveToRight = styled.div`
  margin-left: auto;
`;

const SubmitButton = () => {
  return (
    <MoveToRight>
      <IconButton type="submit" edge="end">
        <SendRoundedIcon />
      </IconButton>
    </MoveToRight>
  );
};

export default SubmitButton;
