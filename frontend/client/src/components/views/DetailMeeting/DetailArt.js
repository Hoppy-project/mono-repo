import React, { useEffect, useState } from "react";
import { Avatar, Icon, Tabs } from "antd";
import InfiniteScroll from "react-infinite-scroll-component";
import { useLocation } from "react-router-dom";
import "./DetailMeetingPage.css";

function DetailArt() {
  // const [Path, setPath] = useState([]);

  const Id = useLocation().pathname;
  const findStr = Id.lastIndexOf("/");
  const test = Id.slice(findStr, 5);
  // console.log("findStr>>>>>>", findStr);

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
            <div style={{ width: "90%", margin: "0 auto" }}>
              <div>
                <Avatar
                  size={27}
                  src="../LandingPage/img/hoppy.png"
                  style={{
                    float: "left",
                    marginRight: "8px",
                  }}
                />
                <p className="left-location">쿼카님</p>
                <Icon
                  type="more"
                  style={{ fontSize: "16px", color: "#A5A5A5", float: "right" }}
                />
              </div>
              <Avatar
                shape="square"
                size={340}
                style={{ marginBottom: "13px" }}
                src="../LandingPage/img/hoppy.png"
              />
              <p className="meeting-text">모임 이름</p>
              <p className="meeting-text">모임 작성 글</p>
              <div style={{ width: "100%", height: "20px" }}>
                <button
                  style={{
                    float: "right",
                    borderRadius: "20px",
                    backgroundColor: "#fff",
                    outline: 0,
                    border: 0,
                  }}
                >
                  <Icon type="heart" style={{ fontSize: "18px" }} />
                </button>
              </div>
              <div
                style={{
                  width: "100%",
                  height: "5px",
                  marginTop: "5px",
                  backgroundColor: "#F1E3D2",
                }}
              />
              {/* 모임 참여 멤버들 */}
              <div style={{ width: "100%", height: "30px", marginTop: "10px" }}>
                <p className="left-location">모임멤버</p>
                <p className="right-location">6명</p>
                <Icon
                  type="user"
                  style={{ fontSize: "18px", float: "right" }}
                />
              </div>
              <div style={{ width: "100%", height: "30px", marginTop: "5px" }}>
                <Avatar
                  size={27}
                  src="../LandingPage/img/hoppy.png"
                  style={{
                    float: "left",
                    marginRight: "8px",
                  }}
                />
                <p className="left-location"> 해피쿼카</p>
                <p className="right-location"> 모임장</p>
              </div>
            </div>
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
