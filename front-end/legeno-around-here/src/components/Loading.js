import React from 'react';
import styled from 'styled-components';
import { makeStyles } from '@material-ui/core/styles';

import ReactLoading from 'react-loading';
import Typography from '@material-ui/core/Typography';

const CenterAlign = styled.div`
  width: 98%;
  margin: auto;
  margin-top: 10px;
  display: flex;
  flex-direction: column;
`;

const TextForWait = styled.div`
  font-size: 25px;
  margin: auto;
`;

const useStyles = makeStyles(() => ({
  loadingStyle: {
    margin: 'auto',
  },
}));

const Loading = () => {
  const classes = useStyles();

  return (
    <CenterAlign>
      <TextForWait>
        <Typography>잠시만 기다려주세요...</Typography>
      </TextForWait>
      <ReactLoading
        type={'bars'}
        color='#0123B4'
        height={'20%'}
        width={'20%'}
        delay={500}
        className={classes.loadingStyle}
      />
    </CenterAlign>
  );
};

export default Loading;
