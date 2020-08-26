import React from 'react';
import styled from 'styled-components';
import IconButton from '@material-ui/core/IconButton';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

import handleBackButtonClicked from '../../util/BackButtonHandler'

const StyledDiv = styled.div`
  margin-right: auto;
  margin-left: 0px;
`;

const BackButton = () => {
  return (
    <StyledDiv onClick={handleBackButtonClicked}>
      <IconButton edge="start">
        <ArrowBackIcon />
      </IconButton>
    </StyledDiv>
  );
};

export default BackButton;
