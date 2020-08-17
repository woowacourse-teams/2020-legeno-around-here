import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
import ListItemText from '@material-ui/core/ListItemText';
import Typography from '@material-ui/core/Typography';

const SectorItem = ({ sector }) => {
  return (
    <>
      <ListItem alignItems="flex-start">
        <ListItemText
          primary={sector.name + ' 부문'}
          secondary={sector.description}
        />
        <div>
          <Typography variant="subtitle1">
            {sector.creator.nickname} 만듦
          </Typography>
          <Typography variant="subtitle1"> 명 도전 중</Typography>
        </div>
      </ListItem>
      <Divider />
    </>
  );
};

export default SectorItem;
