import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import HomeIcon from "@material-ui/icons/Home";
import CreateIcon from "@material-ui/icons/Create";
import CategoryIcon from "@material-ui/icons/Category";
import PersonIcon from "@material-ui/icons/Person";
import EqualizerIcon from "@material-ui/icons/Equalizer";

const useStyles = makeStyles({
  root: {
    width: "100%",
    position: "fixed",
    bottom: 0,
  },
});

export default function LabelBottomNavigation() {
  const classes = useStyles();
  const [value, setValue] = React.useState("home");

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <BottomNavigation
      value={value}
      onChange={handleChange}
      className={classes.root}
      position="fixed"
    >
      <BottomNavigationAction label="홈" value="home" icon={<HomeIcon />} />
      <BottomNavigationAction
        label="랭킹"
        value="rank"
        icon={<EqualizerIcon />}
      />
      <BottomNavigationAction
        label="글쓰기"
        value="writing"
        icon={<CreateIcon />}
      />
      <BottomNavigationAction
        label="부문"
        value="sector"
        icon={<CategoryIcon />}
      />
      <BottomNavigationAction
        label="프로필"
        value="profile"
        icon={<PersonIcon />}
      />
    </BottomNavigation>
  );
}
