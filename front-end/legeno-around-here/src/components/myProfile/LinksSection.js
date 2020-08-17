import React from 'react';
import styled from 'styled-components';
import { Link } from "react-router-dom";

export const NavSection = styled.div`
  width: 97%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background-color: #eeeeee;
`;

export const NavElement = ({ linkTo, children }) => {
  return (
    <StyledLink to={ linkTo }>
      <LinkText>{ children }</LinkText>
    </StyledLink>
  );
};

const StyledLink = styled(Link)`
  height: 70px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-bottom: 5px;
  text-align: left;
  text-decoration: none;
  background-color: #ffffff;

  &:focus, &:hover, &:visited, &:link, &:active {
    text-decoration: none;
  }
`;

const LinkText = styled.div`
  margin-left: 5px;
  color: black;
`;