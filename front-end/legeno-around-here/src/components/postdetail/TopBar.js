import React from 'react';
import styled from 'styled-components';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';

import { MAIN_COLOR } from '../../constants/Color';
import BackButton from './BackButton';
import {
  HEIGHT,
  WIDTH_OF_MOST_LEFT_ITEM,
  HIGHT_OF_MOST_LEFT_ITEM,
  MARGIN_OF_MOST_LEFT_ITEM,
  POSITION_OF_MOST_LEFT_ITEM,
} from '../../constants/TopBar';

const useStyles = makeStyles(() => ({
  topBarBackground: {
    background: MAIN_COLOR,
    height: HEIGHT,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
  },
}));

const ActualItemSpace = styled.div`
  width: ${WIDTH_OF_MOST_LEFT_ITEM};
  height: ${HIGHT_OF_MOST_LEFT_ITEM};
  margin: ${MARGIN_OF_MOST_LEFT_ITEM};
  position: ${POSITION_OF_MOST_LEFT_ITEM};
`;

const TopBar = ({ backButtonLink }) => {
  const classes = useStyles();

  return (
    <AppBar position="sticky" className={classes.topBarBackground}>
      <ActualItemSpace>
        <BackButton linkTo={backButtonLink} />
      </ActualItemSpace>
    </AppBar>
  );
};

export default TopBar;
