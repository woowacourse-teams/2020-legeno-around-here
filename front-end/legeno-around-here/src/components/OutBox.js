import styled from 'styled-components';
import { BACKGROUND_DEFAULT_COLOR } from '../constants/Color';

const OutBox = styled.div`
  @media (max-width: 2000px) {
    width: 360px;
    height: 640px;
    margin-left: auto;
    margin-right: auto;
    border-radius: 4px;
    box-shadow: 2px 2px 10px 3px rgba(0, 0, 0, 0.3);
    background-color: ${BACKGROUND_DEFAULT_COLOR};
  }

  @media (max-width: 450px) {
    width: 100%;
    box-shadow: none;
  }
  
  overflow: hidden;
  display: flex;
  flex-direction: column;
  text-align: center;
  background-color: white;
`;

export default OutBox;
