import React from "react";
import styled from "styled-components";

export const AwardsSection = styled.div`
  width: 90%;
  display: flex;
  align-items: center;
  margin: 20px auto;
`;

const AwardSummaryStyle = styled.div`
  display:flex;
  flex-direction: column;
  margin: auto;
`;

export const AwardSummary = ({ awardName, awardCount }) => {
  return (
    <AwardSummaryStyle>
      <div>{ awardCount }</div>
      <div>{ awardName }</div>
    </AwardSummaryStyle>
  );
};