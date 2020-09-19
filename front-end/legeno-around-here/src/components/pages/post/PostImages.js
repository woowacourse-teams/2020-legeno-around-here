import React from 'react';
import PostImageItem from './PostImageItem';
import Carousel from 'react-material-ui-carousel';

const PostImages = ({ images }) => {
  return (
    <Carousel autoPlay={false} timeout={400}>
      {images.map((image) => (
        <PostImageItem key={image.id} image={image} />
      ))}
    </Carousel>
  );
};

export default PostImages;
