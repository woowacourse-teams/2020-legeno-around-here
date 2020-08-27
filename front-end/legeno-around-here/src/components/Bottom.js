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

import { HOME, PROFILE, WRITING, SECTOR, RANKING } from '../constants/BottomItems';

const bottomWarpStyle = {
  padding: '0px',
};

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
      position='fixed'
    >
      <BottomNavigationAction
        style={bottomWarpStyle}
        component={Link}
        to='/'
        label='홈'
        value={HOME}
        icon={<HomeIcon />}
      />
      <BottomNavigationAction
        style={bottomWarpStyle}
        component={Link}
        to='/ranking'
        label='랭킹'
        value={RANKING}
        icon={<EqualizerIcon />}
      />
      <BottomNavigationAction
        style={bottomWarpStyle}
        component={Link}
        to='/posting'
        label='글쓰기'
        value={WRITING}
        icon={<CreateIcon />}
      />
      <BottomNavigationAction
        style={bottomWarpStyle}
        component={Link}
        to='/sector'
        label='부문'
        value={SECTOR}
        icon={<CategoryIcon />}
      />
      <BottomNavigationAction
        style={bottomWarpStyle}
        component={Link}
        to='/users/me'
        label='프로필'
        value={PROFILE}
        icon={<PersonIcon />}
      />
    </BottomNavigation>
  );
};

export default Bottom;
