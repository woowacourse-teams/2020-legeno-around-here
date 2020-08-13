import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';

const StyledLink = styled(Link)`
  margin-right: auto;
  margin-left: 8px;
`;

const CancelButton = ({ linkTo }) => {
  return (
    <StyledLink to={linkTo}>
      <IconButton edge="start">
        <CloseIcon />
      </IconButton>
    </StyledLink>
  );
};

export default CancelButton;
