/* eslint-disable */
import React, { useEffect, useState } from "react";
import { Avatar, Button, Icon, Tabs } from "antd";
import InfiniteScroll from "react-infinite-scroll-component";
import { useLocation } from "react-router-dom";
import "./DetailMeetingPage.css";
import Axios from "axios";
import FirstDetail from "./FirstDetailPage";

function DetailArt() {
  const [Meeting, setMeeting] = useState([]);

  // 모임 Id 추출
  const url = useLocation().pathname;
  const findIndex = url.lastIndexOf("/");
  const Id = url.slice(findIndex + 1);
  console.log("Id>>>", Id);

  useEffect(() => {
    getMeeting();
  }, []);

  const token = localStorage.getItem("Authorization");
  const headers = {
    Authorization: token,
  };

  async function getMeeting() {
    await Axios.get(`https://hoppy.kro.kr/api/meeting/${Id}`, {
      headers,
      withCredentials: false,
    })
      .then((response) => {
        if (response.data.status === 200) {
          console.log("response>>>>>>", response.data.data);
          setMeeting(response.data.data);
        }
      })
      .catch((error) => {
        console.log("error>>>>>>>", error);
      });
  }

  // 모임 가입 버튼 함수
  const onClickParticipate = (e) => {
    alert("모임에 가입하시겠습니까?");
  };

  return (
    <div
      style={{
        width: "100%",
        textAlign: "center",
      }}
    >
      <div
        style={{
          width: "100%",
          margin: "1rem auto",
        }}
      >
        <Tabs>
          {/* 정보 페이지 */}
          <Tabs.TabPane tab="정보" key="item-1">
            <FirstDetail Meeting={Meeting} />
            <Button
              shape="circle"
              onClick={onClickParticipate}
              style={{
                background: "#D3BA9C",
                width: "60px",
                height: "60px",
                position: "fixed",
                right: 0,
                bottom: 0,
                margin: "0px 15px 50px 0px",
              }}
            >
              가입하기
            </Button>
          </Tabs.TabPane>
          {/* 커뮤니티 페이지 */}
          <Tabs.TabPane tab="커뮤니티" key="item-2">
            Content 2
          </Tabs.TabPane>
          {/* 사진 */}
          <Tabs.TabPane tab="사진" key="item-3">
            Content 3
          </Tabs.TabPane>
        </Tabs>
      </div>
    </div>
  );
}

export default DetailArt;
