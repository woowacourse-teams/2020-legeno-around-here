import React from 'react';
import styled from 'styled-components';

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

export const AwardSummary = ({ awardName, awardCount }) => {
  return (
    <AwardSummaryStyle>
      <div>{awardCount}</div>
      <div>{awardName}</div>
    </AwardSummaryStyle>
  );
};
