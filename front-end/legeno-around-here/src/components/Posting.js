import React from 'react';
import OutBox from './OutBox'
import PostingTopBar from './PostingTopBar'

const Posting = () => {
  return (<>
    <OutBox>
      <PostingTopBar/>
      <button>취소</button>
      <button>등록</button>
      <textarea placeholder="자랑거리를 입력해 주세요"></textarea>
      <button>부문을 추가해주세요</button>
    </OutBox>
  </>);
};

export default Posting;
