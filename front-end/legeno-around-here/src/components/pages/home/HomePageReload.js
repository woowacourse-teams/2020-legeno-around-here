import React, { useEffect } from 'react';

const HomePageReload = ({history}) => {
    useEffect(() => {
        history.replace('/home');
    }, [history]);

  return (
    <>
    </>
  );
};

export default HomePageReload;
