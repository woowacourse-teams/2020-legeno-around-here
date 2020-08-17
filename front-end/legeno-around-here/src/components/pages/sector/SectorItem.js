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
          primary={'부문명 : ' + sector.name}
          secondary={'부문설명 : ' + sector.description}
        />
        <div>
          <Typography variant="subtitle1">
            만든이 : {sector.creator.nickname}
          </Typography>
          <Typography variant="subtitle1">게시글 수 : </Typography>
        </div>
      </ListItem>
      <Divider />
    </>
  );
};

export default SectorItem;
