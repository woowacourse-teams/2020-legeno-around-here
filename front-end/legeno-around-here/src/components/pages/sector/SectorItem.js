import React from 'react';
import Divider from '@material-ui/core/Divider';
import Typography from '@material-ui/core/Typography';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';

const useStyles = makeStyles(() => ({
  text: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    width: '50%',
  },
}));

const SectorItem = ({ sector }) => {
  const classes = useStyles();

  return (
    <>
      <LinkWithoutStyle to={'/sectors/' + sector.id}>
        <Grid container justify='space-between' alignItems='flex-start'>
          <Grid item className={classes.text}>
            <Typography noWrap variant='subtitle1'>
              {sector.name + ' 부문'}
            </Typography>
            <Typography noWrap variant='subtitle2' color='textSecondary'>
              {sector.description}
            </Typography>
          </Grid>
          <Grid item>
            <Typography noWrap variant='subtitle1' display='inline'>
              {sector.creator.nickname}
            </Typography>
            <Typography noWrap variant='subtitle1' display='inline'>
              {'만듦'}
            </Typography>
          </Grid>
        </Grid>
        <Divider />
      </LinkWithoutStyle>
    </>
  );
};

export default SectorItem;
