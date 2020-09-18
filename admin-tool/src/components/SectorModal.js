import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';

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
    width: 800,
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
  },
}));

const SectorModal = ({ open, closeModal, rowId }) => {
  const classes = useStyles();
  const [modalStyle] = React.useState(getModalStyle);

  if (!rowId) {
    return null;
  }

  return (
    <>
      <Modal
        open={open}
        onClose={closeModal}
        aria-labelledby='simple-modal-title'
        aria-describedby='simple-modal-description'
      >
        <div style={modalStyle} className={classes.paper}>
          <h2 id='simple-modal-title'>부문 상세 및 상태 변경</h2>
          <p id='simple-modal-description'>{rowId}</p>
        </div>
      </Modal>
    </>
  );
};

export default SectorModal;
