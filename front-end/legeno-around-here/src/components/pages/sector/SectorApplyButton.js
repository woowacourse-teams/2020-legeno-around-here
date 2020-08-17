import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';

const SectorApplyButton = () => {
  return (
    <Button
      edge="start"
      color="inherit"
      aria-label="open drawer"
      onClick={() => alert('아직 부문 신청 기능이 완성되지 않았습니다!')}
    >
      <Typography variant="h6" noWrap>
        부문신청
      </Typography>
    </Button>
  );
};

export default SectorApplyButton;
