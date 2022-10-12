/* eslint-disable */

import React, {useState, useEffect, useRef} from 'react'
import {Avatar, Button, Input, Form} from 'antd'
import TextArea from 'antd/lib/input/TextArea'
import Axios from 'axios'
import {useDispatch} from 'react-redux'
import {getUser} from '../../../_actions/user_actions'

function EditMyPage(props) {

    const dispatch = useDispatch();
    const [EditUser, setEditUser] = useState([])

    const token = localStorage.getItem('Authorization')

    //placeholder 내용 집어넣기
    useEffect(() => {
        dispatch(getUser()).then(response => {
            setEditUser(response.payload.data)
        })
    }, [dispatch, setEditUser])

    const Intro = EditUser.intro
    const ProfileUrl = EditUser.profileUrl
    const UserName = EditUser.username

    //edit form 제작

    const [EditUserName, setEditUserName] = useState("");
    const [EditProfileUrl, setEditProfileUrl] = useState("")
    const [EditIntro, setEditIntro] = useState("")
    const [File, setFile] = useState([])
    //파일 업로더 생성
    const fileInput = useRef(null)

    //입력 가능하게 event handler 제작
    const editUserNameHandler = (event) => {
        setEditUserName(event.target.value)
    }

    const editProfileUrlHandler = (event) => {
        setEditProfileUrl(event.target.value)
    }

    const editIntroHandler = (event) => {
        setEditIntro(event.target.value)
    }

    const onChange = (event) => {
        if(event.target.files[0]) {
            //업로딩이 되면
            const targetFile = event.target.files[0];
            const name = (event.target.files[0].name) + (event.target.files[0].lastModified)
            const type = event.target.files[0].type
            const headers = {
                Authorization: token
            }
            
            Axios
                .get(`https://hoppy.kro.kr/api/upload/presigned?filename=${name}&contentType=${type}`, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    if(response.data.status === 200) {
                        setFile(response.data.data.url)
                    }
                    const req = new Request(response.data.data.url, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': type,
                        },
                        body: targetFile
                    });
                    return fetch(req);
                })
                .then(response => {
                    console.log('res>>>>>', response)
                    if(response.status === 200) {
                        const url = new URL(response.url)
                        setEditProfileUrl(url.origin + url.pathname)
                        alert("사진 업로드에 성공했습니다.")
                    } else {
                        alert("사진 업로드에 실패했습니다.")
                    }
                })
            setFile(event.target.files[0])
        }
        else {
            //업로드를 취소하면
            return
        }

        console.log('ff', EditProfileUrl)
        //프로필 사진 나타내기
        const reader = new FileReader();
        reader.onload = () => {
            console.log('reader.readyState', reader)
            if(reader.readyState === 2) {
                setFile(reader.result)
            }
        }
        reader.readAsDataURL(event.target.files[0])
    }

    //form 입력 완료 후 전송 값
    const submitHandler = (event) => {
        //페이지 리프레시 방지
        event.preventDefault();
        //state 값이 하나라도 수정되어야 리턴되도록 지정
        if (!EditUserName && !EditIntro && !EditProfileUrl) {
            return alert("수정된 사항이 없습니다.")
        }

        //바뀐 내용을 request 값으로 전송
        
        if (EditUserName && !EditIntro && !EditProfileUrl) {
            const body = {
                username: EditUserName,
                profileUrl: '',
                intro: ''
            }
    
            const headers = {
                Authorization: token
            }
            Axios
                .put("https://hoppy.kro.kr/api/profile", body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('res>>>', response)
                    if (response.data.status === 200) {
                        alert('마이페이지 수정이 완료되었습니다.')
                        props
                            .history
                            .push('/mypage')
                    } else {
                        alert('마이페이지 수정을 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditIntro && !EditProfileUrl && !EditUserName) {
            const body = {
                username: '',
                profileUrl: '',
                intro: EditIntro
            }
            const headers = {
                Authorization: token
            }
            Axios
                .put("https://hoppy.kro.kr/api/profile", body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('res>>>', response)
                    if (response.data.status === 200) {
                        alert('마이페이지 수정이 완료되었습니다.')
                        props
                            .history
                            .push('/mypage')
                    } else {
                        alert('마이페이지 수정을 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditProfileUrl && !EditIntro & !EditUserName) {
            const body = {
                username: '',
                profileUrl: EditProfileUrl,
                intro: ''
            }
            const headers = {
                Authorization: token
            }
            Axios
                .put("https://hoppy.kro.kr/api/profile", body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('res>>>', response)
                    if (response.data.status === 200) {
                        alert('마이페이지 수정이 완료되었습니다.')
                        props
                            .history
                            .push('/mypage')
                    } else {
                        alert('마이페이지 수정을 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditProfileUrl && EditIntro && !EditUserName) {
            const body = {
                username: '',
                profileUrl: EditProfileUrl,
                intro: EditIntro
            }
            const headers = {
                Authorization: token
            }
            Axios
                .put("https://hoppy.kro.kr/api/profile", body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('res>>>', response)
                    if (response.data.status === 200) {
                        alert('마이페이지 수정이 완료되었습니다.')
                        props
                            .history
                            .push('/mypage')
                    } else {
                        alert('마이페이지 수정을 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditProfileUrl && EditUserName && !EditIntro) {
            const body = {
                username: EditUserName,
                profileUrl: EditProfileUrl,
                intro: ''
            }
            const headers = {
                Authorization: token
            }
            Axios
                .put("https://hoppy.kro.kr/api/profile", body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('res>>>', response)
                    if (response.data.status === 200) {
                        alert('마이페이지 수정이 완료되었습니다.')
                        props
                            .history
                            .push('/mypage')
                    } else {
                        alert('마이페이지 수정을 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditIntro && EditUserName && !EditProfileUrl) {
            const body = {
                username: EditUserName,
                profileUrl: '',
                intro: EditIntro
            }
            const headers = {
                Authorization: token
            }
            Axios
                .put("https://hoppy.kro.kr/api/profile", body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('res>>>', response)
                    if (response.data.status === 200) {
                        alert('마이페이지 수정이 완료되었습니다.')
                        props
                            .history
                            .push('/mypage')
                    } else {
                        alert('마이페이지 수정을 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditIntro && EditProfileUrl && EditUserName) {
            const body = {
                username: EditUserName,
                profileUrl: EditProfileUrl,
                intro: EditIntro
            }
            const headers = {
                Authorization: token
            }
            Axios
                .put("https://hoppy.kro.kr/api/profile", body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('res>>>', response)
                    if (response.data.status === 200) {
                        alert('마이페이지 수정이 완료되었습니다.')
                        props
                            .history
                            .push('/mypage')
                    } else {
                        alert('마이페이지 수정을 실패했습니다. 다시 시도해주세요.')
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
                <Form onSubmitCapture={submitHandler}>
                    <div>
                        <p
                            style={{
                                textAlign: 'left',
                                marginLeft: '27px',
                                fontSize: '16px',
                                display: 'inline-block',
                                float: 'left'
                            }}>프로필 수정</p>
                        <Button htmlType='submit'
                            style={{
                                display: 'inline-block',
                                float: 'right',
                                marginRight: '20px',
                                width: '70px',
                                height: '24px',
                                fontSize: '7px',
                                textAlign: 'center',
                                padding: '0px',
                                borderRadius: '10px',
                                background: '#D3BA9C'
                            }}>
                            수정 완료
                        </Button>
                    </div>
                    {/* 사진 수정하기
                    https://velog.io/@kbing14/React-%ED%94%84%EB%A1%9C%ED%95%84-%EC%82%AC%EC%A7%84-%EC%97%85%EB%A1%9C%EB%8D%94-%EB%A7%8C%EB%93%A4%EA%B8%B0 */
                    }
                    <Avatar
                        src={EditProfileUrl?EditProfileUrl:ProfileUrl}
                        onChange={editProfileUrlHandler}
                        value={EditProfileUrl}
                        onClick={() => {fileInput.current.click()}}
                        style={{
                            width: '142px',
                            height: '142px',
                            marginTop: '48px',
                            marginRight: '20px',
                            background: '#A5A5A5'
                        }}/>
                     <input 
                        type='file' 
                        style={{display:'none'}}
                        accept='image/jpg, image/png, image/jpeg' 
                        name='profile_img'
                        onChange={onChange}
                        ref={fileInput}/>
                    <Input
                        placeholder={UserName}
                        onChange={editUserNameHandler}
                        value={EditUserName}
                        style={{
                            width: '138px',
                            height: '25px',
                            textAlign: 'center',
                            marginTop: '16px',
                            borderRadius: '7px'
                        }}/>
                    <p
                        style={{
                            textAlign: 'left',
                            margin: '30px 0px 0px 27px',
                            fontSize: '12px',
                            color: '#858585'
                        }}>다른 사람들에게 자신을 소개해주세요!</p>
                    <TextArea
                        type='textarea'
                        placeholder={Intro}
                        onChange={editIntroHandler}
                        value={EditIntro}
                        maxLength={100}
                        style={{
                            whiteSpace: 'pre-wrap',
                            wordBreak: 'break-all',
                            width: '90%',
                            height: '117px',
                            margin: '18px 20px 0px 20px',
                            fontSize: '11px',
                            color: '#464646',
                            border: '0.8px solid #A5A5A5',
                            borderRadius: '10px'
                        }}></TextArea>
                </Form>
            </div>
        </div>
    )
}

export default EditMyPage