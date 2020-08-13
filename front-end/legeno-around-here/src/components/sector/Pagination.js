import React from 'react';
import styled from 'styled-components';

const PageNumbersStyle = styled.div`
  margin: 5px auto;
`;

const PageNumberStyle = styled.div`
  font-size: 16px;
  font-weight: bold;
  color: black;
  margin: 5px 2px;
  padding: 0px 3px;
  float: left;
  text-align: center;
`;

const Pagination = ({ sectorsPerPage, totalSectors, paginate }) => {
  const pageNumber = [];

  for (let i = 1; i <= Math.ceil(totalSectors / sectorsPerPage); i++) {
    pageNumber.push(i);
  }

  return (
    <PageNumbersStyle>
      {pageNumber.map((pageNum) => (
        <PageNumberStyle key={pageNum} onClick={() => paginate(pageNum)}>
          {pageNum}
        </PageNumberStyle>
      ))}
    </PageNumbersStyle>
  );
};

export default Pagination;
