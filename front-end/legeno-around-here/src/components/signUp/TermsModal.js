import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Modal from '@material-ui/core/Modal';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';

const getModalStyle = () => {
  const top = 50;
  const left = 50;

  return {
    top: `${top}%`,
    left: `${left}%`,
    transform: `translate(-${top}%, -${left}%)`,
  };
};

const useStyles = makeStyles((theme) => ({
  paper: {
    position: 'absolute',
    width: 874,
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
  },
}));

const TermsModal = ({ open, closeModal, agree, setAgree }) => {
  const classes = useStyles();
  const [modalStyle] = React.useState(getModalStyle);

  return (
    <>
      <Modal
        open={open}
        onClose={closeModal}
        aria-labelledby='simple-modal-title'
        aria-describedby='simple-modal-description'
      >
        <div style={modalStyle} className={classes.paper}>
          <h2>우리동네캡짱 이용약관</h2>
          <TextField
            id='outlined-multiline-static'
            label='Multiline'
            multiline
            rows={4}
            defaultValue='Default Value'
            variant='outlined'
            fullWidth
            InputProps={{
              readOnly: true,
            }}
          />
          <Checkbox color='primary' inputProps={{ 'aria-label': 'secondary checkbox' }} />
          우리동네캡짱 이용약관에 동의합니다.(필수)
        </div>
      </Modal>
    </>
  );
};

export default TermsModal;
