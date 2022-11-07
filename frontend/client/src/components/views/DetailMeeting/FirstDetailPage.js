import React, { useEffect, useState } from "react";
import { Avatar, Button, Icon, Tabs } from "antd";
import "./DetailMeetingPage.css";

function FirstDetail(Meeting) {
  console.log("Meeting>>>>>", Meeting.Meeting.Meeting);

  return (
    <div style={{ width: "90%", margin: "0 auto" }}>
      <div>
        <Avatar
          size={27}
          src={Meeting.Meeting.profileImageUrl}
          style={{
            float: "left",
            marginRight: "8px",
          }}
        />
        <p className="left-location">{Meeting.Meeting.ownerName}</p>
        <Icon
          type="more"
          style={{ fontSize: "16px", color: "#A5A5A5", float: "right" }}
        />
      </div>
      <Avatar
        shape="square"
        size={340}
        style={{ marginBottom: "13px" }}
        src={Meeting.Meeting.url}
      />
      <div style={{ width: "300px", margin: "0 auto" }}>
        <h3 className="meeting-TitleText">{Meeting.Meeting.title}</h3>
        <p className="meeting-ContentText">{Meeting.Meeting.content}</p>
      </div>
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
        <Icon type="user" style={{ fontSize: "18px", float: "right" }} />
      </div>
      <div style={{ width: "100%", height: "30px", marginTop: "5px" }}>
        <Avatar
          size={27}
          src={Meeting.Meeting.profileImageUrl}
          style={{
            float: "left",
            marginRight: "8px",
          }}
        />
        <p className="left-location"> {Meeting.Meeting.ownerName}</p>
        <p className="right-location"> 모임장</p>
      </div>
    </div>
  );
}
export default FirstDetail;
