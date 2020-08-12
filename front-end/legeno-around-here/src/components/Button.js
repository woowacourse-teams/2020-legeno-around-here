import React from "react";
import styled from 'styled-components';

const ButtonStyle = styled.button`
  margin-left: 4px;
  padding: 0px 20px;
  height: 50px;
  line-height: 50px;

  border: 1px solid black;
  background-color: white;

  color: black;
  font-size: 15px;
  border-radius: 5px;

  transition: background-color 0.2s ease;

  &:hover {
    background-color: skyblue;
  }

  &:active {
    background-color: white;
  }
`;

const Button = ({ type, children }) => {
  return <ButtonStyle type={type}>{children}</ButtonStyle>;
}

export default Button;
