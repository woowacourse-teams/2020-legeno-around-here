import makeStyles from '@material-ui/core/styles/makeStyles';

const useStyle = makeStyles({
  basicLayout: {
    width: '90%',
    margin: 'auto',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
  },
  infoEditSection: {
    width: '98%',
    margin: 'auto',
  },
  newNicknameInput: {
    margin: 'auto',
    marginTop: '10px',
    width: '98%',
  },
  submit: {
    width: '98%',
    margin: 'auto',
    marginTop: '10px',
  },
});

export default useStyle;
