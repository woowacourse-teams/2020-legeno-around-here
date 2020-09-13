import React, { useEffect } from 'react';

const RankingPageReload = ({history}) => {
    useEffect(() => {
        history.push('/ranking');
    }, [history]);

  return (
    <>
    </>
  );
};

export default RankingPageReload;
