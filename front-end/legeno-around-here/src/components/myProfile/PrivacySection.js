import styled from 'styled-components';

import {MAIN_COLOR} from '../../constants/Color';

export const ProfilePhoto = styled.div`
  width: 100px;
  height: 100px;
  background-image: url(https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png);
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  border-radius: 300px;
  // background-image: url(${props => props.photoUrl});
  border: 1px solid ${MAIN_COLOR};
`;

export const Nickname = styled.div`
  display: inline;
  font-size: 25px;
`;

export const Email = styled.div`
  display: inline;
  font-size: 15px;
`;

export const TopSection = styled.div`
  width: 90%;
  display: flex;
  align-items: center;
  margin: 20px auto;
`;

export const PrivacyBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 20px;
  text-align: left;
`;

export const PrivacyEditBox = styled.div`
  margin-left: auto;
  padding-right: 10px;
  font-size: 15px;
`;
