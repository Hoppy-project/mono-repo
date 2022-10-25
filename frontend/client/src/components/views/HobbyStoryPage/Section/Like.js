import React, { useState } from "react";
import { Icon } from "antd";
import Axios from "axios";

function Like(props) {
  const storyId = props.StoryDetail.id;

  const [Like, setLike] = useState(false);

  const token = localStorage.getItem("Authorization");
  const headers = {
    Authorization: token,
  };

  const clickHeart = () => {
    if (Like) {
      Axios.get(`https://hoppy.kro.kr/api/story/like?id=${storyId}`, {
        headers,
        withCredentials: false,
      }).then((response) => {
        console.log(response);
        if (response.status === 200) {
          setLike(response.data.data.liked);
          window.location.reload(`/hobbystory/${storyId}`);
        } else {
          alert(
            "좋아요 취소가 정상적으로 동작하지 않았습니다. 다시 시도해주세요."
          );
        }
      });
    } else {
      Axios.get(`https://hoppy.kro.kr/api/story/like?id=${storyId}`, {
        headers,
        withCredentials: false,
      }).then((response) => {
        console.log(response);
        if (response.status === 200) {
          setLike(response.data.data.liked);
          window.location.reload(`/hobbystory/${storyId}`);
        } else {
          alert(
            "좋아요 요청이 정상적으로 동작하지 않았습니다. 다시 시도해주세요."
          );
        }
      });
    }
  };

  return (
    <div>
      <button
        onClick={clickHeart}
        style={{ backgroundColor: "#fff", border: 0, outline: 0 }}
      >
        {!Like ? (
          <Icon type="heart" theme="filled" style={{ color: "#D66262" }} />
        ) : (
          <Icon type="heart" theme="outlined" />
        )}
      </button>
    </div>
  );
}

export default Like;
