import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import HomeIcon from '@material-ui/icons/Home';
import ListItemText from '@material-ui/core/ListItemText';
import CategoryIcon from '@material-ui/icons/Category';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import HelpOutlineIcon from '@material-ui/icons/HelpOutline';

const MainListItems = ({ history }) => {
  const moveTo = (event, target) => {
    event.preventDefault();
    history.push(target);
  };

  return (
    <div>
      <ListItem button onClick={(event) => moveTo(event, '/home')}>
        <ListItemIcon>
          <HomeIcon/>
        </ListItemIcon>
        <ListItemText primary="홈"/>
      </ListItem>
      <ListItem button onClick={(event) => moveTo(event, '/sectors')}>
        <ListItemIcon>
          <CategoryIcon/>
        </ListItemIcon>
        <ListItemText primary="부문 관리"/>
      </ListItem>
      <ListItem button onClick={(event) => moveTo(event, '/users')}>
        <ListItemIcon>
          <PersonAddIcon/>
        </ListItemIcon>
        <ListItemText primary="사용자 관리"/>
      </ListItem>
      <ListItem button onClick={(event) => moveTo(event, '/question')}>
        <ListItemIcon>
          <HelpOutlineIcon/>
        </ListItemIcon>
        <ListItemText primary="1:1 문의 관리"/>
      </ListItem>
    </div>
  );
};

export default MainListItems;
