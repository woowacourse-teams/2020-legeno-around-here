import styled from 'styled-components';

export const ProfilePhoto = styled.div`
  width: 100px;
  height: 100px;
  background-color: skyblue;
  border-radius: 300px;
  border: 1px solid blue;
`;

export const Nickname = styled.div`
  display: inline;
  font-size: 28px;
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
`;