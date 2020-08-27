import styled from 'styled-components'
import { Link } from 'react-router-dom'

const LinkWithoutStyle = styled(Link)`
  color: black;
  text-decoration: none;
  
  &:focus,
  &:hover,
  &:visited,
  &:link,
  &:active {
    text-decoration: none;
  }
`;

export default LinkWithoutStyle;
