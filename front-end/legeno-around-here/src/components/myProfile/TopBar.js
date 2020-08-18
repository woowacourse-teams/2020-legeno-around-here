import React from 'react';
import styled from 'styled-components';
import AppBar from '@material-ui/core/AppBar';

import BackButton from './BackButton';
import { ACTUAL_ITEM_SPACE_WIDTH } from '../../constants/TopBar';

const ActualItemSpace = styled.div`
  width: ${ACTUAL_ITEM_SPACE_WIDTH};
  height: 80%;
  margin: 0 auto;
  display: flex;
  flex-direction: row;
`;

const TopBar = ({ backButtonLink }) => {
  return (
    <AppBar position="sticky">
      <ActualItemSpace>
        <BackButton linkTo={backButtonLink} />
      </ActualItemSpace>
    </AppBar>
  );
};

export default TopBar;
