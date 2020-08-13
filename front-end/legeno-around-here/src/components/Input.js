import React from 'react';
import styled from 'styled-components';

const StyledInput = styled.input`
  height: 50px;
  line-height: 50px;
  width: 185px;
  background-color: white;
  border: none;
  border-bottom: 2px solid lightgrey;
  padding: 0 10px;
  margin-bottom: 7px;

  font-size: 15px;
  outline: 0;

  transition: border-color 0.2s ease;

  &:focus {
    border-color: black;
  }

  &::placeholder {
    font-size: 15px;
  }
`;

function Input({ type, placeholder, value, onChange, check }) {
  return (
    <div style={{ display: 'flex' }}>
      <StyledInput
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
      />
      {check}
    </div>
  );
}

export default React.memo(Input);
