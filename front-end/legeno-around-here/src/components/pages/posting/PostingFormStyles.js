import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(0),
  },
  title: {
    display: 'block',
  },
  sectionDesktop: {
    display: 'flex',
  },
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
  list: {
    height: 400,
    overflow: 'auto',
  },
  uploadPhoto: {
    opacity: 0,
    position: 'absolute',
    zIndex: -1,
    pointerEvents: 'none',
  },
  selectSectorButton: {
    display: 'inlineBlock',
    fontSize: '140%',
    color: '#3366bb',
  },
  sector: {
    display: 'inline',
    borderRadius: 100,
    backgroundColor: 'skyblue',
    padding: '5px 8px 5px 8px',
  },
}));

export default useStyles;
