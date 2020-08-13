import React from "react";
import styled from "styled-components";

const InputStyle = styled.input`
  width: 90%;
  height: 40px;
  border: solid 1px #979797;
  background-color: #eeeeee;
  margin: 0 auto;
`;

function Input({ type, placeholder, value, onChange, check }) {
  return (
    <>
      <InputStyle
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
      />
      {check}
    </>
  );
}

export default React.memo(Input);