import React, { useState } from 'react';
import List from '@material-ui/core/List';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Box from '@material-ui/core/Box';

import MySectorItem from './MySectorItem';

const useStyles = makeStyles((theme) => ({
  list: {
    width: '100%',
    backgroundColor: theme.palette.background.paper,
  },
  tab: {
    flexGrow: 1,
    backgroundColor: theme.palette.background.paper,
  },
}));

const TabPanel = (props) => {
  const { value, index, mySectorState, mySectors } = props;

  return (
    <div role='tabpanel' hidden={value !== index} id={`tabpanel-${index}`}>
      {value === index && (
        <Box p={3}>
          {mySectors.map(
            (mySector) => mySector.state === mySectorState && <MySectorItem key={mySector.id} mySector={mySector} />,
          )}
        </Box>
      )}
    </div>
  );
};

const MySectors = ({ mySectors }) => {
  const classes = useStyles();
  const [value, setValue] = useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <div className={classes.tab}>
      <AppBar position='static'>
        <Tabs value={value} onChange={handleChange} variant='fullWidth'>
          <Tab label='승인 신청' />
          <Tab label='승인' />
          <Tab label='반려' />
        </Tabs>
      </AppBar>
      <List className={classes.list}>
        <TabPanel value={value} index={0} mySectorState={'승인 신청'} mySectors={mySectors} />
        <TabPanel value={value} index={1} mySectorState={'승인'} mySectors={mySectors} />
        <TabPanel value={value} index={2} mySectorState={'반려'} mySectors={mySectors} />
      </List>
    </div>
  );
};

export default MySectors;
