import React, { useState } from 'react';
import { Typography } from '@material-ui/core';

const ErrorTypography = ({ content }) => {
  return (
    <Typography variant="caption" color="error">
      {content}
    </Typography>
  );
};

export default ErrorTypography;
