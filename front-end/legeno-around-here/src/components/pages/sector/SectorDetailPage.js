import makeStyles from '@material-ui/core/styles/makeStyles';
import { MAIN_COLOR } from '../../../constants/Color';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import React, { useEffect, useState } from 'react';
import { findMyInfo, findSector } from '../../api/API';
import Loading from '../../Loading';
import PostDetailTopBar from '../post/PostDetailTopBar';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';
import BottomBlank from '../../BottomBlank';
import Button from '@material-ui/core/Button';
import Bottom from '../../Bottom';
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage';
import { Link } from 'react-router-dom';

const useStyle = makeStyles({
  sectorNameSection: {
    margin: '25px auto',
    textAlign: 'center',
  },
  sectorDescriptionSection: {
    margin: '25px auto',
    textAlign: 'center',
  },
  sectorCreatorSection: {
    width: '90%',
    display: 'flex',
    alignItems: 'center',
    margin: '20px auto',
  },
  sectorCreatorSectionBackground: {
    borderRadius: '20px',
    border: '1px solid rgba(20, 80, 200, 0.5)',
    padding: '5px 15px 15px 15px',
  },
  creatorProfilePhoto: (props) => ({
    width: '70px',
    height: '70px',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    borderRadius: '300px',
    backgroundImage: `url(${props.photoUrl})`,
    border: `1px solid ${MAIN_COLOR}`,
  }),
  nickname: {
    display: 'inline',
    fontSize: '25px',
  },
  creatorInfo: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    marginLeft: '20px',
    textAlign: 'left',
    color: 'black',
  },
  buttonSection: {
    width: '99%',
    margin: '30px auto',
    textAlign: 'center',
  },
  goToPostsButton: {
    width: '90%',
    margin: 'auto',
    borderRadius: '12px',
  },
});

const SectorDetailPage = ({ match }) => {
  const sectorId = match.params.sectorId;
  const accessToken = getAccessTokenFromCookie();
  const [sector, secSector] = useState({
    name: '',
    description: '',
    creator: {
      id: null,
      nickname: '',
      image: {
        id: null,
        url: "",
      },
    },
  });
  const [loading, setLoading] = useState(false);
  const [myInfo, setMyInfo] = useState({
    id: null,
  });

  const classes = useStyle({
    photoUrl: sector.creator.image ? sector.creator.image.url : DEFAULT_IMAGE_URL,
  });

  const isSectorCreatorMe = () => sector.creator.id === myInfo.id;

  useEffect(() => {
    const loadMyInfo = async () => {
      setLoading(true);
      const foundMyInfo = await findMyInfo(accessToken);
      setMyInfo(foundMyInfo);
      setLoading(false);
    };

    const loadSector = async () => {
      setLoading(true);
      const sector = await findSector(accessToken, sectorId);
      console.log(sector);
      secSector(sector);
      setLoading(false);
    };
    loadMyInfo();
    loadSector();
  }, [accessToken, sectorId]);

  if (loading) {
    return <Loading />;
  }
  return (
    <>
      <PostDetailTopBar />
      <Container>
        <div className={classes.sectorNameSection}>
          <Typography variant='h4'>{sector.name} 부문</Typography>
        </div>
        <div className={classes.sectorDescriptionSection}>
          <Typography variant='h6'>{sector.description}</Typography>
        </div>
        <div className={classes.sectorCreatorSectionBackground}>
          <Typography>부문 창시자</Typography>
          <LinkWithoutStyle
            to={isSectorCreatorMe() ? '/users/me' : '/users/' + sector.creator.id}
            className={classes.sectorCreatorSection}
          >
            <div className={classes.creatorProfilePhoto} />
            <div className={classes.creatorInfo}>
              <Typography component='h1' variant='h5'>
                <div className={classes.nickname}>{sector.creator.nickname}</div>
              </Typography>
            </div>
          </LinkWithoutStyle>
        </div>
        <div className={classes.buttonSection}>
          <Link to={`/home?sectorId=${sectorId}`}>
            <Button variant='contained' color='primary' className={classes.goToPostsButton} size='large'>
              자랑글 보러 가기
            </Button>
          </Link>
        </div>
        <BottomBlank />
      </Container>
      <Bottom />
    </>
  );
};

export default SectorDetailPage;
