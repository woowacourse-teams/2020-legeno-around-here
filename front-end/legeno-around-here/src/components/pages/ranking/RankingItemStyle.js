import { makeStyles } from '@material-ui/core/styles'
import { MAIN_COLOR } from '../../../constants/Color'

const useStyle = makeStyles({
  grow: {
    flexGrow: 1,
  },
  card: {
    display: 'flex',
  },
  rank: (props) => ({
    width: '65px',
    height: '65px',
    borderRadius: '300px',
    border: `1px solid ${MAIN_COLOR}`,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignContent: 'center',
    textAlign: 'center',
    margin: 'auto 8px',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundImage: `url(${props.prizeImageUrl})`,
  }),
  details: {
    display: 'flex',
    flexDirection: 'column',
    width: '80%',
  },
  content: {
    flex: '1 0 auto',
    padding: '5px',
  },
  cover: {
    flex: '1 0 auto',
    opacity: 0.7,
    backgroundSize: 'contain',
  },
  photoText: {
    textAlign: 'center',
    marginTop: 100,
  },
  reactions: (props) => ({
    display: `${props.whetherToPrintZzangCount ? 'block' : 'none'}`,
    paddingTop: 0,
  }),
  reactionIconSpace: {
    margin: 0,
    padding: 0,
  },
  reactionIcon: {
    width: '20px',
    height: '20px',
  },
  writing: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    width: '190px',
  },
});

export default useStyle;
