import React from "react";
import styled from "styled-components";
import { MAIN_COLOR } from "../../constants/Color";

export const AwardsSection = styled.div`
  width: 90%;
  display: flex;
  align-items: center;
  margin: 20px auto;
  background-color: ${ MAIN_COLOR };
  padding-top: 15px;
  padding-bottom: 15px;
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