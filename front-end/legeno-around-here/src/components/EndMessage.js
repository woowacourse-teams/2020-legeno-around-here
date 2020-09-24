import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
  menuButton: {
    marginTop: '20px',
    marginBottom: '10px',
  },
}));

const EndMessage = ({ message }) => {
  const classes = useStyles();

  return (
    <Grid container justify='center' className={classes.menuButton}>
      <Typography variant='h5'>{message}</Typography>
    </Grid>
  );
};

export default EndMessage;
