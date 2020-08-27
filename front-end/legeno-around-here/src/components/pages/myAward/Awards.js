import React from 'react';
import List from '@material-ui/core/List';
import AwardItem from './AwardItem';
import makeStyles from '@material-ui/core/styles/makeStyles';

const useStyles = makeStyles((theme) => ({
  list: {
    width: '100%',
    backgroundColor: theme.palette.background.paper,
  },
}));

const Awards = ({ awards }) => {
  const classes = useStyles();

  return (
    <List className={classes.list}>
      {awards.map((award, index) => {
        return award && <AwardItem key={index} award={award} />;
      })}
    </List>
  );
};

export default Awards;
