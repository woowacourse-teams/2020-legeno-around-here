import React from 'react';
import Modal from '@material-ui/core/Modal';
import TextField from '@material-ui/core/TextField';
import Checkbox from '@material-ui/core/Checkbox';
import { getModalStyle, useStyles } from './TermsModal';

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
            rows={10}
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
