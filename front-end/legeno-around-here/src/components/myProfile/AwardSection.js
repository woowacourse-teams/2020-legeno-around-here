import React from 'react';
import styled from 'styled-components';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';

export const AwardsSection = styled.div`
  width: 90%;
  display: flex;
  align-items: center;
  margin: 20px auto;
  background-color: #3f51b5;
  padding-top: 15px;
  padding-bottom: 15px;
`;

const AwardSummaryStyle = styled.div`
  display: flex;
  flex-direction: column;
  margin: auto;
`;

const useStyle = makeStyles((theme) => ({
  awardCount: {
    color: 'white',
    textAlign: 'center',
  },
  awardName: {
    color: 'white',
    textAlign: 'center',
  },
}));

export const AwardSummary = ({ awardName, awardsCount }) => {
  const classes = useStyle();

  return (
    <AwardSummaryStyle>
      <Typography component='h4' variant='h4' className={classes.awardCount}>
        {awardsCount}
      </Typography>
      <Typography className={classes.awardName}>{awardName}</Typography>
    </AwardSummaryStyle>
  );
};
