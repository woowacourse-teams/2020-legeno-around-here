import styled from 'styled-components';
import { MAIN_COLOR } from '../../constants/Color';
import { HEIGHT } from '../../constants/TopBar';

const TopBarBackground = styled.div`
  background: ${MAIN_COLOR};
  color: black;
  height: ${HEIGHT};
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default TopBarBackground;
