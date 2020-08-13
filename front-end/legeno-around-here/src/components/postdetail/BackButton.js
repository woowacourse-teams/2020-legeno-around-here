import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import IconButton from '@material-ui/core/IconButton';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

const StyledLink = styled(Link)`
  margin-right: auto;
  margin-left: 0px;
`;

const BackButton = ({ linkTo }) => {
  return (
    <StyledLink to={linkTo}>
      <IconButton edge="start">
        <ArrowBackIcon />
      </IconButton>
    </StyledLink>
  );
};

export default BackButton;
