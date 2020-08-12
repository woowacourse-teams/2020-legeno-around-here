import styled from 'styled-components';
import { MAIN_COLOR } from '../../constants/Color';
import { TOP_BAR_HEIGHT } from '../../constants/Size';

const TopBarBackground = styled.div`
  background: ${ MAIN_COLOR };
  color: black;
  height: ${ TOP_BAR_HEIGHT };
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default TopBarBackground;
