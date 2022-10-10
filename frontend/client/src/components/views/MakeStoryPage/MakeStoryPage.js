/* eslint-disable */

import React, {useState, useRef} from 'react'
import {Input, Button, Icon, Avatar, Form} from 'antd'
import TextArea from 'antd/lib/input/TextArea'
import './style.css'
import Axios from 'axios'

function HobbyStoryPage(props) {

    const token = localStorage.getItem('Authorization')

    //form 제작
    const [Title, setTitle] = useState("")
    const [Content, setContent] = useState("")
    const [FileName, setFileName] = useState("")
    const [File, setFile] = useState([])
    const [Name, setName] = useState("")
    //파일 업로더 생성
    const fileInput = useRef(null)

    //event-handler 제작
    const titleHandler = (event) => {
        setTitle(event.target.value)
    }
    const contentHandler = (event) => {
        setContent(event.target.value)
    }
    const fileNameHandler = (event) => {
        setFileName(event.target.value)
    }

    const onChange = (event) => {
        if (event.target.files[0]) {
            //업로딩이 되면
            console.log('event.target.files[0]', event.target.files[0])
            setName(event.target.files[0].name)
            const targetFile = event.target.files[0];
            const name = (event.target.files[0].name) + (event.target.files[0].lastModified)
            const type = event.target.files[0].type
            const headers = {
                Authorization: token
            }

            Axios
                .get(
                    `https://hoppy.kro.kr/api/upload/presigned?filename=${name}&contentType=${type}`,
                    {headers, withCredentials: false}
                )
                .then(response => {
                    console.log('resres', response)
                    if (response.data.status === 200) {
                        setFile(response.data.data.url)
                    }
                    const req = new Request(response.data.data.url, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': type
                        },
                        body: targetFile
                    });
                    console.log('req', req)
                    return fetch(req);
                })
                .then(response => {
                    console.log('res>>>>>', response)
                    if (response.status === 200) {
                        const url = new URL(response.url)
                        console.log(url.origin + url.pathname)
                        setFileName(url.origin + url.pathname)
                        alert("사진 업로드에 성공했습니다.")
                    } else {
                        alert("사진 업로드에 실패했습니다.")
                    }
                })
            setFile(event.target.files[0].name)
        } else {
            return
        }
        //프로필 사진 나타내기
        const reader = new FileReader();
        reader.onload = () => {
            console.log('reader.readyState', reader)
            if (reader.readyState === 2) {
                setFile(reader.result)
            }
        }
        reader.readAsDataURL(event.target.files[0])
    }

    //글 올리기 버튼 event-handler
    const submitHandler = (event) => {
        event.preventDefault();
        if (!Title && !Content) {
            return alert("제목과 내용을 입력해주세요.")
        }
        if (Title && Content && FileName === "") {
            const body = {
                title: Title,
                content: Content,
                filename: FileName
            }
            const headers = {
                Authorization: token
            }
            Axios
                .post(
                    "https://hoppy.kro.kr/api/story",
                    body,
                    {headers, withCredentials: false}
                )
                .then(response => {
                    console.log('res>>>>', response)
                    if (response.data.status === 200) {
                        alert('스토리를 업로드했습니다.')
                        props
                            .history
                            .push('/hobbystory')
                    } else {
                        alert('스토리 업로드에 실패했습니다.')
                    }
                })
        } else if (Title && Content && FileName) {
            const body = {
                title: Title,
                content: Content,
                filename: FileName
            }
            const headers = {
                Authorization: token
            }
            Axios
                .post(
                    "https://hoppy.kro.kr/api/story",
                    body,
                    {headers}
                )
                .then(response => {
                    console.log('res>>>>', response)
                    if (response.data.status === 200) {
                        alert('스토리를 업로드했습니다.')
                        props
                            .history
                            .push('/hobbystory')
                    } else {
                        alert('스토리 업로드에 실패했습니다.')
                    }
                })
        }
    }

    return (
        <div
            style={{
                textAlign: 'center',
                width: '100%'
            }}>
            <div
                style={{
                    width: '100%',
                    margin: '3rem auto'
                }}>
                <p
                    style={{
                        textAlign: 'left',
                        marginLeft: '27px',
                        fontSize: '16px'
                    }}>취미 스토리</p>
                <Form onSubmitCapture={submitHandler}>
                    <div>
                        <Input
                            placeholder="제목을 작성해주세요."
                            onChange={titleHandler}
                            value={Title}
                            style={{
                                width: "350px",
                                height: "46px",
                                background: "#F0F0F0",
                                borderRadius: "14px",
                                margin: "10px 10px 16px 11px",
                                fontSize: "12px"
                            }}/>
                        <TextArea
                            type='textarea'
                            placeholder="글을 작성해주세요."
                            onChange={contentHandler}
                            value={Content}
                            style={{
                                width: "350px",
                                height: "282px",
                                background: "#F0F0F0",
                                borderRadius: "14px",
                                margin: "0px 10px 16px 11px",
                                fontSize: "12px",
                                whiteSpace: 'pre-wrap',
                                wordBreak: 'break-all'
                            }}
                            maxLength={1000}/>
                        <label htmlFor='image-button'>
                            <Icon
                                type="camera"
                                style={{
                                    fontSize: "15px",
                                    marginRight: '8px'
                                }}/>
                            사진 첨부하기</label>
                        <input
                            type='file'
                            id='image-button'
                            style={{
                                display: 'none'
                            }}
                            accept='image/jpg, image/png, image/jpeg'
                            name='story_img'
                            onChange={onChange}
                            ref={fileInput}/>
                        <div
                            style={{
                                width: '350px',
                                height: '75px',
                                border: '1px solid',
                                color: 'gray',
                                borderRadius: '11px',
                                margin: '16px 20px 0px 11px',
                                padding: '9px',
                                display: 'flex',
                            }}>
                            <Avatar
                                shape="square"
                                size={55}
                                src={FileName}
                                onChange={fileNameHandler}
                                value={FileName}/>
                                <p style={{width: '250px', marginTop: '12px'}}>
                                    {Name}
                                </p>
                            <Icon
                                type='delete'
                                style={{
                                    fontSize: '20px',
                                    float: 'right',
                                    marginTop: '15px'
                                }}/>
                        </div>
                        <Button
                            htmlType='submit'
                            style={{
                                width: '128px',
                                height: '38px',
                                background: '#D3BA9C',
                                borderRadius: '19px',
                                marginTop: '38px'
                            }}>
                            글 올리기
                        </Button>
                    </div>
                </Form>
            </div>
        </div>
    )
}

export default HobbyStoryPage