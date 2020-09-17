import React from 'react';
import clsx from 'clsx';
import Box from '@material-ui/core/Box';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Profit from '../components/Profit';
import Payments from '../components/Payments';
import Copyright from '../components/Copyright';
import MenuBar from '../components/MenuBar';
import Token from '../components/validation/Token';
import mainPageStyle from '../libs/mainPageStyle';

const HomePage = () => {
  const classes = mainPageStyle();
  const fixedHeightPaper = clsx(classes.paper, classes.fixedHeight);

  return (
    <div className={classes.root}>
      <Token />
      <MenuBar menuName='Home' />
      <main className={classes.content}>
        <div className={classes.appBarSpacer} />
        <Container maxWidth='lg' className={classes.container}>
          <Grid container spacing={3}>
            {/* Chart */}
            <Grid item xs={12} md={8} lg={9}>
              <Paper className={fixedHeightPaper}>
                <div>홈 - 이것 저것 모아서 보여주면 좋을 것 같은 페이지, 아래들은 Mock 데이터(기분내봤음)</div>
              </Paper>
            </Grid>
            {/* Recent Profit */}
            <Grid item xs={12} md={4} lg={3}>
              <Paper className={fixedHeightPaper}>
                <Profit />
              </Paper>
            </Grid>
            {/* Recent Payments */}
            <Grid item xs={12}>
              <Paper className={classes.paper}>
                <Payments />
              </Paper>
            </Grid>
          </Grid>
          <Box pt={4}>
            <Copyright />
          </Box>
        </Container>
      </main>
    </div>
  );
};

export default HomePage;
