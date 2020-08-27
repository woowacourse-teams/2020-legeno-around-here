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
import { findAllSimpleSectors, getUnreadNoticeCount } from '../../api/API';
import AreaSearch from '../../AreaSearch';
import { Link } from 'react-router-dom';

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
  const [unreadNotice, setUnreadNotice] = useState(0);

  useEffect(() => {
    const loadAllSimpleSectors = async () => {
      const foundSimpleSectors = await findAllSimpleSectors(accessToken);
      await setSimpleSectors(foundSimpleSectors);
    };
    loadAllSimpleSectors();
  }, [accessToken]);

  useEffect(() => {
    getUnreadNoticeCount(accessToken, setUnreadNotice);
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
          <div className={classes.sectionDesktop}>
            <Link to='/notice'>
              <IconButton aria-label='show 17 new notifications' color='inherit'>
                <Badge badgeContent={unreadNotice} color='secondary'>
                  <NotificationsIcon style={{ color: 'white' }} />
                </Badge>
              </IconButton>
            </Link>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
};

export default HomeTopBar;
