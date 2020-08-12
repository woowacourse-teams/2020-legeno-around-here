import styled from "styled-components";

const Button = styled.button`
  width: 90%;
  height: 40px;
  font-size: 16px;
  font-weight: bold;
  line-height: 40px;
  background-color: #bcbcbc;
  outline: 0;
  margin: 5px auto;
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

export default Button;