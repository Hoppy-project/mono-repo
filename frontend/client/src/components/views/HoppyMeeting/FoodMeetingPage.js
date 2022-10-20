/* eslint-disable */
import React, { useEffect, useState } from "react";
import { Input, Icon } from "antd";
import Axios from "axios";
import InfiniteScroll from "react-infinite-scroll-component";

import Heart from "../LandingPage/img/heart.png";
import HeartFilled from "../LandingPage/img/heart_click.png";

function FoodMeetingPage() {
  const { Search } = Input;

  const onSearch = (value) => console.log(value);

  const [like, setLike] = useState(false);

  // 무한스크롤
  const [MeetingList, setMeetingList] = useState([]);
  const [Fetching, setFetching] = useState(false);
  const [FetchData, setFetchData] = useState("");
  const [NextpagingUrl, setNextpagingUrl] = useState("");

  const categoryNumber = 5; // 음식 카테고리
  const token = localStorage.getItem("Authorization");
  const headers = {
    Authorization: token,
  };

  // 로그인 확인 알림
  if (token == null) {
    alert("로그인 후 이용해주세요");
  }
  async function getMeetingList() {
    await Axios.get(
      `https://hoppy.kro.kr/api/meeting?categoryNumber=${categoryNumber}`,
      {
        headers,
        withCredentials: false,
      }
    ).then((response) => {
      if (response.data.status === 200 && response.data !== undefined) {
        console.log("response>>>>>", response.data.data);
        setMeetingList(response.data.data.meetingList);
        setNextpagingUrl(response.data.data.nextPagingUrl);
      } else {
        alert("데이터 불러오기를 실패했습니다.");
      }
    });
  }

  useEffect(() => {
    getMeetingList();
  }, []);

  const InfiniteScrollHandler = () => {
    // 추가 데이터 불러오는 함수
    if (NextpagingUrl === null) {
      console.log("더 이상 조회할 데이터가 없습니다.");
      setFetching(false);
    } else if (NextpagingUrl === "end") {
      console.log("더 이상 조회할 데이터가 없습니다.");
      setFetching(false);
    } else {
      // 데이터 기져오기
      Axios.get(NextpagingUrl, {
        headers,
        withCredentials: false,
      }).then((response) => {
        console.log("추가 데이터>>>>>>", response.data);
        const addData = response.data.data.meetingList;
        const mergeData = MeetingList.concat(addData);
        setMeetingList(mergeData); // 가져온 데이터
        setFetchData(addData.length); // 추가해줄 데이터 길이
        setNextpagingUrl(response.data.data.nextPagingUrl); // 데이터를 불러올 다음 url
      });
    }
  };

  console.log(Fetching);

  const MoreLoad = () => {
    // 추가 데이터 유무 함수
    if (FetchData < 14) {
      setFetching(false);
    } else if (FetchData === 14) {
      setFetching(true);
    }
  };

  const MeetingCard = MeetingList.map((meeting, index) => {
    // 실제 보여지는 데이터
    console.log("meeting>>>>>>", meeting);

    const onClickMeeting = (e) => {
      // meeting 클릭 시 해당 모임 페이지로 매칭
      window.location.href = "/exerciseMeeting/detail";
    };

    const onClickHeart = (e) => {
      // 하트 색 변경 함수
      setLike(!like);
    };
    return (
      <div
        key={index}
        style={{
          float: "left",
          width: "46%",
          height: "220px",
          marginRight: "2%",
          marginLeft: "2%",
          marginBottom: "20px",
          border: "0.8px solid #A5A5A5",
          borderRadius: "8px",
        }}
      >
        <img
          src={meeting.url}
          alt="hoppy"
          onClick={onClickMeeting}
          style={{
            width: "150px",
            height: "150px",
            marginTop: "9px",
            float: "center",
            display: "inlineBlock",
          }}
        />
        <p
          style={{
            fontSize: "13px",
            margin: "9px ",
            float: "left",
          }}
        >
          {meeting.title}
        </p>
        <div
          style={{
            width: "100%",
            height: "20px",
            float: "left",
            fontSize: "16px",
          }}
        >
          <Icon
            type="user"
            style={{
              float: "left",
              marginLeft: "10px",
            }}
          />
          <p style={{ fontSize: "13px", float: "left" }}>
            {meeting.participants}명
          </p>
          {like ? (
            <img
              src={HeartFilled}
              alt="heart"
              onClick={onClickHeart}
              style={{
                float: "right",
                marginRight: "10px",
                marginBottom: "10px",
              }}
            />
          ) : (
            <img
              src={Heart}
              alt="heart"
              onClick={onClickHeart}
              style={{
                width: "23px",
                float: "right",
                marginRight: "10px",
                marginTop: "-3px",
              }}
            />
          )}
        </div>
      </div>
    );
  });

  return (
    <div
      style={{
        textAlign: "center",
        width: "100%",
      }}
    >
      <div
        style={{
          width: "100%",
          margin: "3rem auto",
        }}
      >
        <Search
          placeholder="찾으시는 취미를 검색해보세요!"
          onSearch={onSearch}
          style={{
            textAlign: "center",
            width: "90%",
          }}
          // enterButton
          size="large"
        />
        <h3
          style={{
            float: "left",
            paddingTop: "26px",
            fontSize: "16px",
            marginLeft: "27px",
            marginRight: "27px",
          }}
        >
          음식 모임 리스트
          <span role="img" aria-label="daily">
            🍽
          </span>
        </h3>
        {/* 모임 리스트 조회 */}
        <div style={{ width: "95%", margin: "70px auto" }}>
          <InfiniteScroll
            dataLength={MeetingList.length} // 반복되는 컴포넌트의 개수
            next={InfiniteScrollHandler} // 스크롤이 바닥에 닿으면 데이터를 더 불러오는 함수
            hasMore={MoreLoad} // 추가 데이터 유무
          >
            {MeetingCard}
          </InfiniteScroll>
        </div>
      </div>
    </div>
  );
}

export default FoodMeetingPage;
