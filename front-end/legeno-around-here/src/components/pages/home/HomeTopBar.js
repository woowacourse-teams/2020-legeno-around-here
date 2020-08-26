import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import NotificationsIcon from '@material-ui/icons/Notifications';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { findAllSimpleSectors } from '../../api/API';
import AreaSearch from '../../AreaSearch';

const useStyles = makeStyles(() => ({
  grow: {
    flexGrow: 1,
  },
  flex: {
    display: 'flex',
  },
}));

const HomeTopBar = ({ setSectorId }) => {
  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [simpleSectors, setSimpleSectors] = useState([]);

  useEffect(() => {
    const loadAllSimpleSectors = async () => {
      const foundSimpleSectors = await findAllSimpleSectors(accessToken);
      await setSimpleSectors(foundSimpleSectors);
    };
    loadAllSimpleSectors();
  }, [accessToken]);

  return (
    <>
      <AppBar position='sticky'>
        <Toolbar>
          <AreaSearch />
          <Autocomplete
            id='sector-search'
            freeSolo
            options={simpleSectors}
            onChange={(event, option) => option && setSectorId(option.id)}
            getOptionLabel={(option) => option.name}
            fullWidth
            renderInput={(params) => <TextField {...params} placeholder='부문을 검색하세요!' />}
          />
          <div className={classes.grow} />
          <div className={classes.flex}>
            <IconButton
              aria-label='show 17 new notifications'
              color='inherit'
              onClick={() => {
                alert('아직 알람기능이 완성되지 않았습니다!');
              }}
            >
              <Badge badgeContent={0} color='secondary'>
                <NotificationsIcon />
              </Badge>
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
};

export default HomeTopBar;
