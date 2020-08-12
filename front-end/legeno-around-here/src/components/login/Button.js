import styled from 'styled-components';

const ButtonStyle = styled.button`
  width: 320px;
  height: 40px;
  font-size: 16px;
  font-weight: bold;
  line-height: 40px;
  background-color: #bcbcbc;
  border: 1px solid black;
  background-color: white;
  color: black;
  margin: 5px auto;
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

export default ButtonStyle;
