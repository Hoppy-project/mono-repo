import React, { useState, useRef } from "react";
import { Input, Button, Icon, Form } from "antd";
import Axios from "axios";
import TextArea from "antd/lib/input/TextArea";
import ImageIcon from "../../LandingPage/img/trip.png";
import "./UploadMeeting.css";

function UploadTrip() {
  // 토큰 가져오기
  const token = localStorage.getItem("Authorization");
  console.log("token>>>>>", token);

  // 모임 생성 form
  const [UploadTitle, setUploadTitle] = useState("");
  const [UploadContent, setUploadContent] = useState("");
  const [UploadCategory, setUploadCategory] = useState(1);
  const [UploadMemberLimit, setUploadMemberLimit] = useState(1);

  // Img 파일을 미리 볼 수 있게 하는 state
  const [FileImage, setFileImage] = useState("");
  const [FileName, setFileName] = useState("");

  // 파일 업로더 생성
  const fileInput = useRef(null);

  // file upload 시 onchange 함수
  const onChangeUploadImg = (event) => {
    // 파일이 업로드 됐을 때
    if (event.target.files[0]) {
      const targetFile = event.target.files[0];
      const filename =
        event.target.files[0].name + event.target.files[0].lastModified;
      const filetype = event.target.files[0].type;

      setFileName(event.target.files[0].name); // file 이름 설정

      const headers = {
        Authorization: token,
      };

      Axios.get(
        `https://hoppy.kro.kr/api/upload/presigned?filename=${filename}&contentType=${filetype}`,
        {
          headers,
          withCredentials: false,
        }
      )
        .then((response) => {
          if (response.data.status === 200) {
            // setFile(response.data.data.url);
          }
          const req = new Request(response.data.data.url, {
            method: "PUT",
            headers: {
              "Content-Type": filetype,
            },
            body: targetFile,
          });
          return fetch(req);
        })
        .then((response) => {
          console.log("res>>>>>>", response);
          if (response.status === 200) {
            const url = new URL(response.url);
            setFileImage(url.origin + url.pathname); // presignedURL 발급받음
            alert("사진 업로드에 성공했습니다.");
          } else {
            alert("사진 업로드에 실패했습니다.");
          }
        });
      // setFile(event.target.files[0]);
    } else {
      // 업로드 취소했을 때
      return;
    }
  };

  // file upload 시 onClick 함수
  const onClickUploadImg = (event) => {
    fileInput.current.click();
  };

  const titleChangeHandler = (event) => {
    setUploadTitle(event.currentTarget.value);
  };

  const memberLimitChangeHandler = (event) => {
    setUploadMemberLimit(event.currentTarget.value);
  };

  const contentChangeHandler = (event) => {
    setUploadContent(event.currentTarget.value);
  };

  const deleteImgHandler = (event) => {
    // img 삭제
  };

  // 폼 post 함수
  const submitHandler = (event) => {
    event.preventDefault();

    // 여행 카테고리 6번 설정
    setUploadCategory(6);

    // 입력이 안된 항목 예외처리
    if (!UploadTitle) {
      alert("모임 이름을 입력해주세요");
    } else if (!UploadMemberLimit) {
      alert("정원을 입력해주세요");
    } else if (!UploadContent) {
      alert("멤버 모집 글을 작성해주세요");
    } else if (!FileName) {
      alert("대표 이미지를 등록해주새요");
    } else {
      //
      const body = {
        category: UploadCategory,
        title: UploadTitle,
        content: UploadContent,
        memberLimit: UploadMemberLimit,
        url: FileImage,
      };

      const headers = {
        Authorization: token,
      };

      Axios.post(`https://hoppy.kro.kr/api/meeting`, body, {
        headers,
        withCredentials: false,
      })
        .then((response) => {
          if (response.data.status === 200) {
            alert("모임이 생성되었습니다.");
            // 운동 모임 모집 글로 매칭
            window.location.href("/");
          }
          console.log("form submit>>>>>", response);
        })
        .catch((error) => {
          console.log("form error>>>>>", error);
        });
    }
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
        <h3
          style={{
            textAlign: "left",
            marginLeft: "27px",
          }}
        >
          취미 모임 만들기
        </h3>
        {/* 폼 제출 */}
        <Form onSubmitCapture={submitHandler}>
          <div>
            <img
              src={ImageIcon}
              alt="운동"
              style={{
                width: "32px",
                height: "32px",
                float: "left",
                margin: "42px 0px 0px 27px",
              }}
            />
            <Input
              className="title-input"
              placeholder="모임의 이름을 입력해주세요."
              onChange={titleChangeHandler}
              style={{
                width: "295px",
                height: "46px",
                background: "#F0F0F0",
                borderRadius: "14px",
                margin: "36px 10px 0px 11px",
                fontSize: "12px",
              }}
            />
            <Input
              placeholder="정원을 입력해주세요."
              // prefix={<Icon type = 'user' />}
              onChange={memberLimitChangeHandler}
              type="Number"
              style={{
                width: "349px",
                height: "46px",
                background: "#F0F0F0",
                borderRadius: "14px",
                margin: "16px 20px 0px 20px",
                fontSize: "12px",
              }}
            />
            {/* 사진 업로드 */}
            <Button
              onClick={onClickUploadImg}
              style={{
                width: "349px",
                height: "46px",
                background: "#FFFFFF",
                margin: "16px 20px 0px 20px",
                borderRadius: "14px",
                color: "#000000",
                textAlign: "center",
                fontSize: "12px",
                fontWeight: "10px",
              }}
            >
              <Icon
                type="camera"
                style={{
                  fontSize: "17px",
                }}
              />
              대표 사진 등록하기
            </Button>
            <input
              type="file"
              onChange={onChangeUploadImg}
              ref={fileInput}
              multiple={false}
              style={{ display: "none" }}
              accept="image/jpg, image/png, image/jpeg"
              name="meeting_img"
            />
            <div className="preview">
              {FileImage && (
                <div className="imgUpload_div">
                  <img
                    src={FileImage}
                    alt={FileName}
                    style={{
                      width: "60px",
                      height: "60px",
                      margin: "16px 20px 0px 20px",
                      float: "left",
                      display: "inline-block",
                    }}
                  />
                  <p
                    style={{
                      fontSize: "15px",
                      marginTop: "30px",
                      display: "inline-block",
                    }}
                  >
                    {FileName}
                  </p>
                  <Icon
                    type="delete"
                    style={{
                      display: "inline-block",
                      float: "right",
                      fontSize: "15px",
                      marginTop: "30px",
                    }}
                    onClick={deleteImgHandler}
                  />
                </div>
              )}
            </div>
            {/* 모집글 작성 */}
            <TextArea
              placeholder="모임의 멤버를 모집하는 글을 작성해주세요."
              onChange={contentChangeHandler}
              type="text"
              style={{
                width: "349px",
                height: "254px",
                background: "#F0F0F0",
                borderRadius: "14px",
                margin: "16px 20px 0px 20px",
                fontSize: "13px",
              }}
            />
            {/* 제출 버튼 */}
            <Button
              htmlType="submit"
              style={{
                width: "128px",
                height: "38px",
                background: "#D3BA9C",
                borderRadius: "19px",
                color: "#464646",
                margin: "0 auto",
                marginTop: "30px",
              }}
            >
              글 올리기
            </Button>
          </div>
        </Form>
      </div>
    </div>
  );
}

export default UploadTrip;
