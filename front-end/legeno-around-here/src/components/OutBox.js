import styled from 'styled-components';

const OutBox = styled.div`
  @media (max-width: 2000px) {
    width: 450px;
    height: 600px;
    margin-left: auto;
    margin-right: auto;
    border-radius: 4px;
    box-shadow: 2px 2px 10px 3px rgba(0, 0, 0, 0.3);
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
