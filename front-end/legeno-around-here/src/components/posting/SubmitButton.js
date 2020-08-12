import styled from 'styled-components';

import submit from '../../images/submit.png';

const SubmitButton = styled.button`
  background: url(${submit}) no-repeat;
  background-size: 30px;
  width: 30px;
  height: 30px;
  margin-right: 8px;
  margin-left: auto;
  border: 0;
  outline: 0;
  cursor: pointer;
`;

export default SubmitButton;
