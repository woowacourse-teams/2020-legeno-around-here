import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';
import { useCookies } from 'react-cookie';
import { findSector, updateSectorState } from '../api/SectorApi';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';
import IconButton from '@material-ui/core/IconButton';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';
import produce from 'immer';

const currencies = [
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
  const [rerenderStandard, setRerenderStandard] = useState(false);
  const defaultStateAndReason = {
    state: '승인',
    reason: '검토 완료',
  };
  const [updateStateAndReason, setUpdateStateAndReason] = useState(defaultStateAndReason);

  useEffect(() => {
    if (rowId) {
      const sectorDetail = async () =>
        await findSector(cookies, removeCookie, closeModal, rowId, setLoading, setSectorDetails);
      sectorDetail();
    }
    // eslint-disable-next-line
  }, [open, rerenderStandard]);

  if (!rowId) {
    return null;
  }

  if (rowId && loading) {
    return <div>loading</div>;
  }

  const onChangeState = (event) => {
    event.preventDefault();
    setUpdateStateAndReason(
      produce(updateStateAndReason, (draft) => {
        draft['state'] = event.currentTarget.dataset.value;
      }),
    );
  };

  const onChangeReason = (event) => {
    event.preventDefault();
    setUpdateStateAndReason(
      produce(updateStateAndReason, (draft) => {
        draft['reason'] = event.currentTarget.value;
      }),
    );
  };

  const updateState = (event) => {
    event.preventDefault();
    const sectorDetail = async () =>
      await updateSectorState(
        cookies,
        removeCookie,
        rowId,
        updateStateAndReason,
        defaultStateAndReason,
        setUpdateStateAndReason,
        rerenderStandard,
        setRerenderStandard,
      );
    sectorDetail();
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
          <h2>부문 상세</h2>
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
            label='상태'
            defaultValue={sectorDetails.state}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          &ensp;
          <TextField
            label='사유'
            defaultValue={sectorDetails.reason}
            InputProps={{
              readOnly: true,
            }}
            variant='outlined'
          />
          <br />
          <br />
          <hr />
          <h2 id='simple-modal-title'>부문 상태 변경</h2>
          <TextField
            label='변경 상태'
            defaultValue={defaultStateAndReason.state}
            select
            helperText='변경 희망 상태를 고르세요.'
            variant='outlined'
            onChange={onChangeState}
          >
            {currencies.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.value}
              </MenuItem>
            ))}
          </TextField>
          &ensp;
          <TextField
            label='변경 사유'
            defaultValue={defaultStateAndReason.reason}
            helperText='상태를 변경하는 사유를 적어주세요. (생략 가능)ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ'
            variant='outlined'
            onChange={onChangeReason}
          />
          &ensp;
          <IconButton color='primary' aria-label='Save State' onClick={updateState}>
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
