import React from 'react';
import styled from 'styled-components';
import Typography from '@material-ui/core/Typography';

import Bottom from '../Bottom';
import {RANKING} from '../../constants/BottomItems';
import {MAIN_COLOR} from '../../constants/Color';

const Card = styled.div`
  width: 95%;
  height: 140px;
  background-color: #f6f6f6;
  margin: auto;
  margin-top: 7px;
  margin-bottom: 7px;
  display: flex;
  justify-content: center;
  align-content: center;
`;

const Rank = styled.div`
  width: 70px;
  height: 70px;
  border: 1px solid ${MAIN_COLOR};
  border-radius: 500px;
  margin: auto;
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
  text-align: center;
`;

const TopSection = styled.div`
  width: 98%;
  padding-top: 30px;
  padding-bottom: 30px;
`;

const RankingPage = () => {
  return (
    <>
      <TopSection>
        <Typography>대한민국</Typography>
        <Typography>캡짱은 누구?</Typography>
      </TopSection>
      <Card>
        <Rank>1</Rank>
      </Card>
      <Card>
        <Rank>2</Rank>
      </Card>
      <Bottom selected={RANKING} />
    </>
  );
};

export default RankingPage;
