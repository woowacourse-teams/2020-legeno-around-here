import React from 'react';
import { Link } from 'react-router-dom';
import IconButton from '@material-ui/core/IconButton';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

const BackButton = ({ linkTo }) => {
  return (
    <Link to={linkTo}>
      <IconButton edge="start">
        <ArrowBackIcon />
      </IconButton>
    </Link>
  );
};

export default BackButton;
