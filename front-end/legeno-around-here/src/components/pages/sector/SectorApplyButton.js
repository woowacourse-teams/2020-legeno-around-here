import React, { useState } from 'react';
import { Button, Typography } from '@material-ui/core';
import SectorApplyModal from './SectorApplyModal';

const SectorApplyButton = () => {
  const [open, setOpen] = useState(false);

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      <Button
        edge="start"
        color="inherit"
        aria-label="open drawer"
        onClick={handleOpen}
      >
        <Typography variant="h6" noWrap>
          부문신청
        </Typography>
      </Button>
      <SectorApplyModal open={open} handleClose={handleClose} />
    </>
  );
};

export default SectorApplyButton;
