import React from 'react';
import styled from 'styled-components';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';

import { MAIN_COLOR } from '../../constants/Color';
import CancelButton from './CancelButton';
import Submit from './SubmitButton';
import { HEIGHT, ACTUAL_ITEM_SPACE_WIDTH } from '../../constants/TopBar';

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
  width: ${ACTUAL_ITEM_SPACE_WIDTH};
  height: 80%;
  margin: '0 auto';
  display: flex;
  flex-direction: row;
`;

const TopBar = ({ cancelButtonLink }) => {
  const classes = useStyles();

  return (
    <AppBar position="sticky" className={classes.topBarBackground}>
      <ActualItemSpace>
        <CancelButton linkTo={cancelButtonLink} />
        <Submit />
      </ActualItemSpace>
    </AppBar>
  );
};

export default TopBar;
