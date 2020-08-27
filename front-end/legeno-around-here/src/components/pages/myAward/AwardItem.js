import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
import ListItemText from '@material-ui/core/ListItemText';
import Typography from '@material-ui/core/Typography';
import { Link } from 'react-router-dom';

const AwardItem = ({ award }) => {
  return (
    <>
      <ListItem alignItems='flex-start'>
        <ListItemText primary={award.name + ' 부문'} secondary={award.date} />
        <div>
          <Link to={award.location}>
            <Typography>부문 상세</Typography>
          </Link>
        </div>
      </ListItem>
      <Divider />
    </>
  );
};

export default AwardItem;
