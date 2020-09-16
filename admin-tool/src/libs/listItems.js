import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import HomeIcon from '@material-ui/icons/Home';
import CategoryIcon from '@material-ui/icons/Category';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import HelpOutlineIcon from '@material-ui/icons/HelpOutline';
import ReportIcon from '@material-ui/icons/Report';
import PostAddIcon from '@material-ui/icons/PostAdd';
import TocIcon from '@material-ui/icons/Toc';
import PersonIcon from '@material-ui/icons/Person';

export const mainListItems = (
  <div>
    <ListItem button>
      <ListItemIcon>
        <HomeIcon/>
      </ListItemIcon>
      <ListItemText primary="홈"/>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <CategoryIcon/>
      </ListItemIcon>
      <ListItemText primary="부문 관리"/>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <PersonAddIcon/>
      </ListItemIcon>
      <ListItemText primary="사용자 권한 관리"/>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <HelpOutlineIcon/>
      </ListItemIcon>
      <ListItemText primary="1:1 문의 관리"/>
    </ListItem>
  </div>
);

export const secondaryListItems = (
  <div>
    <ListSubheader inset>
      신고 관리
    </ListSubheader>
    <ListItem button>
      <ListItemIcon>
        <PostAddIcon/>
        <ReportIcon/>
      </ListItemIcon>
      <ListItemText primary="글 신고"/>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <TocIcon/>
        <ReportIcon/>
      </ListItemIcon>
      <ListItemText primary="댓글 신고"/>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <CategoryIcon/>
        <ReportIcon/>
      </ListItemIcon>
      <ListItemText primary="부문 신고"/>
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <PersonIcon/>
        <ReportIcon/>
      </ListItemIcon>
      <ListItemText primary="사용자 신고"/>
    </ListItem>
  </div>
);