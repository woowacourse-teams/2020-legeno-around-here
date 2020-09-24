import React, { useEffect, useState } from 'react';
import Modal from '@material-ui/core/Modal';
import Backdrop from '@material-ui/core/Backdrop';
import Fade from '@material-ui/core/Fade';
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import InfiniteScroll from 'react-infinite-scroll-component';
import PostingFormSectors from './PostingFormSectors';
import { findSectorsFromPage } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import EndMessage from '../../EndMessage';

const useStyles = makeStyles((theme) => ({
  modal: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
  },
  selectSectorButton: {
    display: 'inlineBlock',
    fontSize: '140%',
    color: '#3366bb',
  },
}));

const DEFAULT_SIZE = 10;

const PostingFormSectorSearch = ({ setSector, history }) => {
  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(false);
  const [open, setOpen] = useState(false);
  const [sectors, setSectors] = useState([]);

  const loadNextSectors = async () => {
    try {
      const nextSectors = await findSectorsFromPage(page, accessToken, history);
      if (nextSectors.length < DEFAULT_SIZE) {
        setSectors(sectors.concat(nextSectors));
        setPage(page + 1);
        setHasMore(false);
        return;
      }
      setSectors(sectors.concat(nextSectors));
      setPage(page + 1);
    } catch (error) {
      setHasMore(false);
    }
  };

  useEffect(() => {
    setHasMore(true);
    loadNextSectors();
    // eslint-disable-next-line
  }, []);

  const selectSector = (sector) => {
    setSector(sector);
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      <Button onClick={handleOpen} className={classes.selectSectorButton}>
        부문 설정
      </Button>
      <Modal
        aria-labelledby='transition-modal-title'
        aria-describedby='transition-modal-description'
        className={classes.modal}
        open={open}
        onClose={handleClose}
        closeAfterTransition
        BackdropComponent={Backdrop}
        BackdropProps={{
          timeout: 500,
        }}
      >
        <Fade in={open}>
          <div className={classes.paper}>
            <InfiniteScroll
              next={loadNextSectors}
              hasMore={hasMore}
              loader={<Loading />}
              dataLength={sectors.length}
              height={'400px'}
              endMessage={<EndMessage message={'모든 부문을 확인하셨습니다!'} />}
            >
              {sectors && sectors.length > 0 && <PostingFormSectors selectSector={selectSector} sectors={sectors} />}
            </InfiniteScroll>
          </div>
        </Fade>
      </Modal>
    </>
  );
};

export default PostingFormSectorSearch;
