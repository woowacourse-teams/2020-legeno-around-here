import React from 'react';
import Link from '@material-ui/core/Link';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Title from './Title';

const preventDefault = (event) => {
  event.preventDefault();
};

const useStyles = makeStyles({
  depositContext: {
    flex: 1,
  },
});

const Profit = () => {
  const classes = useStyles();
  return (
    <React.Fragment>
      <Title>우리동네캡짱 수익</Title>
      <Typography component='p' variant='h4'>
        1,000,000$
      </Typography>
      <Typography color='textSecondary' className={classes.depositContext}>
        on 17 September, 2022
      </Typography>
      <div>
        <Link color='primary' href='#' onClick={preventDefault}>
          수익 내역 상세보기
        </Link>
      </div>
    </React.Fragment>
  );
};

export default Profit;
