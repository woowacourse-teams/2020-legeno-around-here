import React from 'react';
import { Link } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import HomeIcon from '@material-ui/icons/Home';
import CreateIcon from '@material-ui/icons/Create';
import CategoryIcon from '@material-ui/icons/Category';
import PersonIcon from '@material-ui/icons/Person';
import EqualizerIcon from '@material-ui/icons/Equalizer';

import { HOME, PROFILE, WRITING, SECTOR, RANK } from '../constants/BottomItems';

const useStyles = makeStyles({
  bottomBarStyle: {
    width: '100%',
    position: 'fixed',
    bottom: 0,
    borderTop: '1px solid #dddddd',
  },
});

const Bottom = ({ selected }) => {
  const classes = useStyles();
  const [value, setValue] = React.useState(selected);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <BottomNavigation
      value={value}
      onChange={handleChange}
      className={classes.bottomBarStyle}
      showLabels
      position="fixed"
    >
      <BottomNavigationAction
        component={Link}
        to="/"
        label="홈"
        value={HOME}
        icon={<HomeIcon />}
      />
      <BottomNavigationAction
        component={Link}
        to="/"
        label="랭킹"
        value={RANK}
        icon={<EqualizerIcon />}
      />
      <BottomNavigationAction
        component={Link}
        to="/posting"
        label="글쓰기"
        value={WRITING}
        icon={<CreateIcon />}
      />
      <BottomNavigationAction
        component={Link}
        to="/sector"
        label="부문"
        value={SECTOR}
        icon={<CategoryIcon />}
      />
      <BottomNavigationAction
        component={Link}
        to="/mypage"
        label="프로필"
        value={PROFILE}
        icon={<PersonIcon />}
      />
    </BottomNavigation>
  );
};

export default Bottom;
