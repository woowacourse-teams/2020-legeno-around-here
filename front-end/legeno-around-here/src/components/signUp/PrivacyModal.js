import React from 'react';
import Modal from '@material-ui/core/Modal';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
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

const PrivacyModal = ({ open, closeModal, agree, setAgree }) => {
  const classes = useStyles();
  const [modalStyle] = React.useState(getModalStyle);

  const onChangeAgree = (event) => {
    event.preventDefault();
    const turnedAgree = !agree;
    setAgree(turnedAgree);
    if (turnedAgree) {
      closeModal();
    }
  };

  return (
    <>
      <Modal
        open={open}
        onClose={closeModal}
        aria-labelledby='simple-modal-title'
        aria-describedby='simple-modal-description'
      >
        <div style={modalStyle} className={classes.paper}>
          <h2>우리동네캡짱 개인정보 수집 및 이용 안내</h2>
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
          <Checkbox
            color='primary'
            inputProps={{ 'aria-label': 'secondary checkbox' }}
            checked={agree}
            onChange={onChangeAgree}
          />
          개인정보 수집 및 이용에 동의합니다.(필수)
        </div>
      </Modal>
    </>
  );
};

export default PrivacyModal;
