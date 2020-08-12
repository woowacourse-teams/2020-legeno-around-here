import React, { useState, useEffect } from "react";
import axios from "axios";
import { getAccessTokenFromCookie } from "../util/TokenUtils";
import Posts from "./sector/Posts";
import Pagination from "./sector/Pagination";
import OutBox from "./OutBox";

function SectorPage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [postsPerPage] = useState(9);

  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = posts.slice(indexOfFirstPost, indexOfLastPost);
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  useEffect(() => {
    setLoading(true);
    const config = {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        "X-Auth-Token": accessToken,
      },
    };
    axios
      .get("http://localhost:8080/sectors?size=50", config)
      .then(async (response) => {
        const userResponse = await response.data;
        setPosts(userResponse.content);
        console.log(userResponse.content);
      })
      .catch((error) => {
        alert(`부문정보를 가져올 수 없습니다.${error}`);
      });
    setLoading(false);
  }, [accessToken]);
  if (loading) return <div>Loading...</div>;
  return (
    <OutBox>
      <Posts posts={currentPosts} />
      <Pagination
        postsPerPage={postsPerPage}
        totalPosts={posts.length}
        paginate={paginate}
      />
    </OutBox>
  );
}

export default SectorPage;
