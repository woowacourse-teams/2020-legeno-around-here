import React, { useEffect } from 'react';

const HomePageReload = ({history}) => {
    useEffect(() => {
        history.push('/home');
    }, [history]);

  return (
    <>
    </>
  );
};

export default HomePageReload;
