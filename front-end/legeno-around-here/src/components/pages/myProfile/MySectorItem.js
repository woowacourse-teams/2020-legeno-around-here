import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
import ListItemText from '@material-ui/core/ListItemText';
import Typography from '@material-ui/core/Typography';

const MySectorItem = ({ mySector }) => {
  return (
    <>
      <ListItem alignItems="flex-start">
        <ListItemText
          primary={mySector.name + ' 부문'}
          secondary={mySector.description}
        />
        <div>
          {/*<Typography variant="subtitle1">*/}
          {/*  현재 부문 상태 : {mySector.state}*/}
          {/*</Typography>*/}
          {mySector.state === '반려'
            && <Typography variant="subtitle1">사유 : {mySector.reason}</Typography>}
        </div>
      </ListItem>
      <Divider />
    </>
  );
};

export default MySectorItem;
