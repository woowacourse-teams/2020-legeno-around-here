import React from 'react';
import styled from 'styled-components';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import LinkWithoutStyle from '../../util/LinkWithoutStyle'

export const NavSection = styled.div`
  width: 97%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background-color: #eeeeee;
`;

const useStyle = makeStyles((theme) => ({
  textStyle: {
    color: 'black',
    fontSize: '18px',
  },
}));

export const NavElement = ({ linkTo, children }) => {
  const classes = useStyle();

  return (
    <StyledLink to={linkTo}>
      <Typography className={classes.textStyle}>{children}</Typography>
    </StyledLink>
  );
};

const StyledLink = styled(LinkWithoutStyle)`
  height: 70px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-bottom: 5px;
  text-align: left;
  background-color: #ffffff;
`;
