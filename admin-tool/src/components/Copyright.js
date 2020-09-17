import React from 'react';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';

const Copyright = () => {
  return (
    <Typography variant='body2' color='textSecondary' align='center'>
      {'Copyright © '}
      <Link color='inherit' href='https://capzzang.co.kr'>
        ITTABI
      </Link>{' '}
      2020.08
      {'.'}
    </Typography>
  );
};

export default Copyright;
