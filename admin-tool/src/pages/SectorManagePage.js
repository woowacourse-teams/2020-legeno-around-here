import React from 'react';
import mainPageStyle from '../libs/mainPageStyle';
import clsx from 'clsx';
import Token from '../components/validation/Token';
import MenuBar from '../components/MenuBar';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import Copyright from '../components/Copyright';

const SectorManagePage = ({ history }) => {
  const classes = mainPageStyle();
  const fixedHeightPaper = clsx(classes.paper, classes.paperFullHeight);

  return (
    <div className={classes.root}>
      <Token history={history}/>
      <MenuBar menuName='Sector Management' history={history}/>
      <main className={classes.content}>
        <div className={classes.appBarSpacer}/>
        <Container maxWidth="lg" className={classes.container}>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <Paper className={fixedHeightPaper}>
                <div>부문 관리 - 부문 승인 요청을 승인/반려하는 페이지</div>
              </Paper>
            </Grid>
          </Grid>
          <Box pt={4}>
            <Copyright/>
          </Box>
        </Container>
      </main>
    </div>
  );
};

export default SectorManagePage;
