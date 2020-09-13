import React, { useEffect } from 'react';

const RankingPageReload = ({history}) => {
    useEffect(() => {
        history.replace('/ranking');
    }, [history]);

  return (
    <>
    </>
  );
};

export default RankingPageReload;
