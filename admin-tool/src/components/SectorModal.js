import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';
import { useCookies } from 'react-cookie';
import { findSector } from '../api/SectorApi';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';
import IconButton from '@material-ui/core/IconButton';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';

const currencies = [
  {
    value: '등록',
  },
  {
    value: '승인 신청',
  },
  {
    value: '승인',
  },
  {
    value: '반려',
  },
];

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

const SectorModal = ({ open, closeModal, rowId }) => {
  const classes = useStyles();
  const [modalStyle] = React.useState(getModalStyle);
  const [cookies, removeCookie] = useCookies(['accessToken']);
  const [sectorDetails, setSectorDetails] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    console.log('useEffect - 1');
    if (rowId) {
      const sectorDetail = async () => await findSector(cookies, removeCookie, rowId, setLoading, setSectorDetails);
      sectorDetail();
    }
    // eslint-disable-next-line
  }, [open]);

  if (loading) {
    return <>로딩중</>;
  }

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
          <TextField
            label='부문명'
            defaultValue={sectorDetails.name}
            fullWidth
            placeholder='Placeholder'
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          <br />
          <br />
          <TextField
            label='부문 상세'
            fullWidth
            placeholder='Placeholder'
            defaultValue={sectorDetails.description}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          <br />
          <br />
          <TextField
            label='등록 일시'
            defaultValue={sectorDetails.createdAt}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          &ensp;
          <TextField
            label='등록자 ID'
            defaultValue={sectorDetails.creatorId}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          &ensp;
          <TextField
            label='등록자 Email'
            defaultValue={sectorDetails.creatorEmail}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          &ensp;
          <TextField
            label='등록자 Nickname'
            defaultValue={sectorDetails.creatorNickname}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          <br />
          <br />
          <TextField
            label='최종 수정 일시'
            defaultValue={sectorDetails.lastModifiedAt}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          &ensp;
          <TextField
            label='최종 수정자 ID'
            defaultValue={sectorDetails.lastModifierId}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          &ensp;
          <TextField
            label='최종 수정자 Email'
            defaultValue={sectorDetails.lastModifierEmail}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          &ensp;
          <TextField
            label='최종 수정자 Nickname'
            defaultValue={sectorDetails.lastModifierNickname}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          <br />
          <br />
          <TextField
            label={'상태 (기존 값 : ' + sectorDetails.state + ')'}
            defaultValue={sectorDetails.state}
            select
            helperText='변경 희망 상태를 고르세요.'
            variant='outlined'
          >
            {currencies.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.value}
              </MenuItem>
            ))}
          </TextField>
          &ensp;
          <TextField
            label={'사유 (기존 값 : ' + sectorDetails.reason + ')'}
            placeholder='상태 변경 사유'
            defaultValue={sectorDetails.reason}
            helperText='상태를 변경하는 사유를 적어주세요. (생략 가능)ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ'
            variant='outlined'
          />
          &ensp;
          <IconButton color='primary' aria-label='Save State'>
            <DoneIcon />
          </IconButton>
          <IconButton color='secondary' aria-label='Close modal' onClick={closeModal}>
            <ClearIcon />
          </IconButton>
        </div>
      </Modal>
    </>
  );
};

export default SectorModal;
