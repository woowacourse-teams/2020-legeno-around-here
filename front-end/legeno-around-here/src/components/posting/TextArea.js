import React from 'react';
import styled from 'styled-components';

const TextInput = styled.textarea`
  width: 98%;
  height: 200px;
  padding: 10px;
  margin: 5px auto 5px auto;
  border: none;
  font-size: 120%;
  background-color: rgba(0, 0, 0, 0);
  
  &:focus {
    outline: none;
  }
`;

export default TextInput;
