import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import React from 'react';

const EndMessage = ({ message }) => {
  return (
    <Grid container justify='center'>
      <Typography variant='h5'>{message}</Typography>
    </Grid>
  );
};

export default EndMessage;
