import React from 'react';
import ListSubheader from '@material-ui/core/ListSubheader';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import PostAddIcon from '@material-ui/icons/PostAdd';
import ReportIcon from '@material-ui/icons/Report';
import ListItemText from '@material-ui/core/ListItemText';
import TocIcon from '@material-ui/icons/Toc';
import CategoryIcon from '@material-ui/icons/Category';
import PersonIcon from '@material-ui/icons/Person';

const secondaryListItems = ({ history }) => {
  const moveTo = (event, target) => {
    event.preventDefault();
    history.push(target);
  };

  return (
    <div>
      <ListSubheader inset>신고 관리</ListSubheader>
      <ListItem button link='/post-report' onClick={(event) => moveTo(event, '/post-report')}>
        <ListItemIcon>
          <PostAddIcon />
          <ReportIcon />
        </ListItemIcon>
        <ListItemText primary='글 신고' />
      </ListItem>
      <ListItem button link='/comment-report' onClick={(event) => moveTo(event, '/comment-report')}>
        <ListItemIcon>
          <TocIcon />
          <ReportIcon />
        </ListItemIcon>
        <ListItemText primary='댓글 신고' />
      </ListItem>
      <ListItem button link='/sector-report' onClick={(event) => moveTo(event, '/sector-report')}>
        <ListItemIcon>
          <CategoryIcon />
          <ReportIcon />
        </ListItemIcon>
        <ListItemText primary='부문 신고' />
      </ListItem>
      <ListItem button link='/user-report' onClick={(event) => moveTo(event, '/user-report')}>
        <ListItemIcon>
          <PersonIcon />
          <ReportIcon />
        </ListItemIcon>
        <ListItemText primary='사용자 신고' />
      </ListItem>
    </div>
  );
};

export default secondaryListItems;
