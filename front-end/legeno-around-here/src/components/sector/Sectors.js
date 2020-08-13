import React from 'react';
import styled from 'styled-components';

const SectorsStyle = styled.div`
  font-size: 16px;
  height: 588px;
  font-weight: bold;
  background-color: #bcbcbc;
  border: 1px solid black;
  color: black;
  margin: 5px auto;
  padding: 0px 3px;
  border-radius: 1px;
`;

const SectorStyle = styled.div`
  width: 320px;
  height: 60px;
  font-size: 16px;
  font-weight: bold;
  background-color: #bcbcbc;
  border: 1px solid black;
  background-color: white;
  color: black;
  margin: 3px auto;
  font-size: 15px;
  border-radius: 1px;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: skyblue;
  }

  &:active {
    background-color: white;
  }
`;

const SectorTitleStyle = styled.div`
  margin-left: 10px;
  height: 30px;
  line-height: 30px;
  text-align: left;
`;

const SectorDescriptionStyle = styled.div`
  margin-left: 10px;
  height: 30px;
  line-height: 30px;
  text-align: left;
`;

const Sectors = ({ sectors, loading }) => {
  if (loading) {
    return <h2>Loading...</h2>;
  }
  return (
    <SectorsStyle className="list">
      {sectors.map((sector) => (
        <SectorStyle key={sector.id}>
          <SectorTitleStyle>{sector.name}</SectorTitleStyle>
          <SectorDescriptionStyle>{sector.description}</SectorDescriptionStyle>
        </SectorStyle>
      ))}
    </SectorsStyle>
  );
};

export default Sectors;
