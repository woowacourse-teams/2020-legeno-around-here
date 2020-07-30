import React from "react";

function Input({ type, placeholder, value, onChange, check }) {
  return (
    <div style={{ display: "flex" }}>
      <input
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
      />
      {check}
    </div>
  );
}

export default React.memo(Input);
