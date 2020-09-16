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

const QuestionManagePage = ({ history }) => {
  const classes = mainPageStyle();
  const fixedHeightPaper = clsx(classes.paper, classes.fixedHeight);

  return (
    <div className={classes.root}>
      <Token history={history}/>
      <MenuBar menuName='Question Management' history={history}/>
      <main className={classes.content}>
        <div className={classes.appBarSpacer}/>
        <Container maxWidth="lg" className={classes.container}>
          <Grid container spacing={3}>
            {/* Chart */}
            <Grid item xs={12}>
              <Paper className={fixedHeightPaper}>
                <div>질문 관리 - 1:1 질문에 답변할 수 있는 페이지</div>
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

export default QuestionManagePage;
