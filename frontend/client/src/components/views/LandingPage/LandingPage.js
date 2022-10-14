import React from "react";
import { Button, Input } from "antd";

import CharacterImg from "./img/character.png";
import ArtImg from "./img/art.png";
import DailyImg from "./img/daily.png";
import ExerciseImg from "./img/exercise.png";
import FoodImg from "./img/food.png";
import HobbyStoryImg from "./img/hobbyStory.png";
import MusicImg from "./img/music.png";
import TripImg from "./img/trip.png";

function LandingPage() {
  const { Search } = Input;
  const onSearch = (value) => console.log(value);

  const token = localStorage.getItem("Authorization");
  console.log("token is >>>", token);

  const hobbyBoxStyle = {
    width: "108px",
    height: "108px",
    // border: "solid 0.8px #A5A5A5",
    borderRadius: "15px",
    display: "inline-block",
    margin: "7px",
    padding: "10px",
  };

  const hobbyImageStyle = {
    width: "62px",
    height: "62px",
  };

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

        <h4
          style={{
            textAlign: "left",
            color: "#787878",
            paddingTop: "30px",
            marginLeft: "27px",
          }}
        >
          취미 카테고리
        </h4>

        <a
          href="/exerciseMeeting"
          style={{ textDecoration: "none", color: "#000" }}
        >
          <div style={hobbyBoxStyle}>
            <img src={ExerciseImg} alt="운동" style={hobbyImageStyle} />
            <div>운동</div>
          </div>
        </a>
        <a href="/artMeeting" style={{ textDecoration: "none", color: "#000" }}>
          <div style={hobbyBoxStyle}>
            <img src={ArtImg} alt="공예" style={hobbyImageStyle} />
            <div>공예</div>
          </div>
        </a>
        <a
          href="/musicMeeting"
          style={{ textDecoration: "none", color: "#000" }}
        >
          <div style={hobbyBoxStyle}>
            <img src={MusicImg} alt="음악" style={hobbyImageStyle} />
            <div>음악</div>
          </div>
        </a>
        <a
          href="/dailyMeeting"
          style={{ textDecoration: "none", color: "#000" }}
        >
          <div style={hobbyBoxStyle}>
            <img src={DailyImg} alt="일상" style={hobbyImageStyle} />
            <div>일상</div>
          </div>
        </a>
        <a
          href="/foodMeeting"
          style={{ textDecoration: "none", color: "#000" }}
        >
          <div style={hobbyBoxStyle}>
            <img src={FoodImg} alt="음식" style={hobbyImageStyle} />
            <div>음식</div>
          </div>
        </a>
        <a
          href="/tripMeeting"
          style={{ textDecoration: "none", color: "#000" }}
        >
          <div style={hobbyBoxStyle}>
            <img src={TripImg} alt="여행" style={hobbyImageStyle} />
            <div>여행</div>
          </div>
        </a>

        <h4
          style={{
            padding: "35px 0px 10px 0px",
            marginLeft: "27px",
            textAlign: "left",
            color: "#787878",
          }}
        >
          취미를 함께 할 사람들을 찾아보세요
        </h4>
        <div
          style={{
            backgroundColor: "#F7F0E9",
            // border: "0.8px solid #A5A5A5",
            width: "350px",
            height: "144px",
            borderRadius: "10px",
            margin: "0px auto",
          }}
        >
          <div
            style={{
              float: "left",
              display: "inline-block",
              paddingTop: "28px",
              paddingLeft: "36px",
            }}
          >
            <p
              style={{
                fontSize: "19px",
                color: "#988165",
              }}
            >
              취미 모임
            </p>
            <Button
              href="/makeMeeting"
              style={{
                width: "128px",
                height: "38px",
                background: "#D3BA9C",
                borderRadius: "19px",
                textDecoration: "none",
                color: "#000",
              }}
            >
              모임 만들기
            </Button>
          </div>
          <img
            src={CharacterImg}
            alt="쿼카"
            style={{
              width: "42%",
              marginTop: "30px",
              display: "inline-block",
            }}
          />
        </div>

        <h4
          style={{
            padding: "35px 0px 10px 0px",
            marginLeft: "27px",
            textAlign: "left",
            color: "#787878",
          }}
        >
          취미를 기록하고 공유해보세요
        </h4>
        <div
          style={{
            backgroundColor: "#F7F0E9",
            // border: "0.8px solid #A5A5A5",
            width: "350px",
            height: "144px",
            borderRadius: "10px",
            margin: "0px auto",
          }}
        >
          <div
            style={{
              float: "left",
              display: "inline-block",
              paddingTop: "28px",
              paddingLeft: "36px",
            }}
          >
            <p
              style={{
                fontSize: "19px",
                color: "#988165",
              }}
            >
              일상을 기록하는
            </p>
            <Button
              href="/hobbystory"
              style={{
                width: "128px",
                height: "38px",
                background: "#D3BA9C",
                borderRadius: "19px",
                textDecoration: "none",
                color: "#000",
              }}
            >
              취미 스토리
            </Button>
          </div>
          <img
            src={HobbyStoryImg}
            alt="Hoppy"
            style={{
              width: "126px",
              height: "131px",
              display: "inline-block",
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
