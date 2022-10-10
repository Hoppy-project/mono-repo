/* eslint-disable */

import React, {useEffect, useRef, useState} from 'react'
import Axios from 'axios'
import { Form, Input, Icon, Avatar, Button } from 'antd'
import TextArea from 'antd/lib/input/TextArea'

function EditDetailStoryPage(props) {

    //수정할 스토리 가져오기
    const [StoryContent, setStoryContent] = useState([])

    const storyId = props.match.params.storyId

    const token = localStorage.getItem('Authorization')
    const headers = {
        Authorization: token
    }
    
    async function getStoryContent() {
        await Axios
            .get(`https://hoppy.kro.kr/api/story/detail?id=${storyId}`, {
                headers,
                withCredentials: false
            })
            .then(response => {
                if (response.status === 200) {
                    setStoryContent(response.data.data)
                } else {
                    alert("스토리를 불러오는 데 실패했습니다.")
                }
            })
    }

    useEffect(() => {
        getStoryContent()
    }, [])

    console.log(StoryContent)
    console.log('ff', StoryContent.filename)

    let imagename = "" + StoryContent.filename
    let imageName = imagename.substring(53)
    console.log(imageName)

    //edit form
    const [EditTitle, setEditTitle] = useState('')
    const [EditContent, setEditContent] = useState('')
    const [EditFileName, setEditFileName] = useState('')

    const fileInput = useRef(null)

    const editTitleHandler = (event) => {
        setEditTitle(event.target.value)
    }

    const editContentHandler = (event) => {
        setEditContent(event.target.value)
    }

    const editFileNameHandler = (event) => {
        setEditFileName(event.target.value)
    }

    const [Name, setName] = useState('')
    const [File, setFile] = useState('')

    const onChange = (event) => {
        if (event.target.files[0]) {
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
                        setEditFileName(url.origin + url.pathname)
                        alert("사진 업로드에 성공했습니다.")
                    } else {
                        alert("사진 업로드에 실패했습니다.")
                    }
                })
            setFile(event.target.files[0].name)
        } else {
            return
        }
        //수정된 스토리 사진 나타내기
        const reader = new FileReader();
        reader.onload = () => {
            console.log('reader.readyState', reader)
            if (reader.readyState === 2) {
                setFile(reader.result)
            }
        }
        reader.readAsDataURL(event.target.files[0])
    }

    const submitHandler = (event) => {
        event.preventDefault();

        if (!EditTitle && !EditContent && !EditFileName) {
            return alert("수정된 사항이 없습니다.")
        }
        if (EditTitle && !EditContent && !EditFileName) {
            const body = {
                title: EditTitle,
                content: StoryContent.content,
                filename: StoryContent.filename
            }

            const headers = {
                Authorization: token
            }

            Axios
                .put(`https://hoppy.kro.kr/api/story?id=${storyId}`, body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('response >>', response)
                    if (response.status === 200) {
                        alert('스토리 수정이 완료되었습니다.')
                        props
                            .history
                            .push(`/hobbystory/${storyId}`)
                    } else {
                        alert('스토리 수정에 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (!EditTitle && EditContent && !EditFileName) {
            const body = {
                title: StoryContent.title,
                content: EditContent,
                filename: StoryContent.filename
            }

            const headers = {
                Authorization: token
            }

            Axios
                .put(`https://hoppy.kro.kr/api/story?id=${storyId}`, body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('response >>', response)
                    if (response.status === 200) {
                        alert('스토리 수정이 완료되었습니다.')
                        props
                            .history
                            .push(`/hobbystory/${storyId}`)
                    } else {
                        alert('스토리 수정에 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (!EditTitle && !EditContent && EditFileName) {
            const body = {
                title: StoryContent.title,
                content: StoryContent.content,
                filename: EditFileName
            }

            const headers = {
                Authorization: token
            }

            Axios
                .put(`https://hoppy.kro.kr/api/story?id=${storyId}`, body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('response >>', response)
                    if (response.status === 200) {
                        alert('스토리 수정이 완료되었습니다.')
                        props
                            .history
                            .push(`/hobbystory/${storyId}`)
                    } else {
                        alert('스토리 수정에 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditTitle && EditContent && !EditFileName) {
            const body = {
                title: EditTitle,
                content: EditContent,
                filename: StoryContent.filename
            }

            const headers = {
                Authorization: token
            }

            Axios
                .put(`https://hoppy.kro.kr/api/story?id=${storyId}`, body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('response >>', response)
                    if (response.status === 200) {
                        alert('스토리 수정이 완료되었습니다.')
                        props
                            .history
                            .push(`/hobbystory/${storyId}`)
                    } else {
                        alert('스토리 수정에 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditTitle && !EditContent && EditFileName) {
            const body = {
                title: EditTitle,
                content: StoryContent.content,
                filename: EditFileName
            }

            const headers = {
                Authorization: token
            }

            Axios
                .put(`https://hoppy.kro.kr/api/story?id=${storyId}`, body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('response >>', response)
                    if (response.status === 200) {
                        alert('스토리 수정이 완료되었습니다.')
                        props
                            .history
                            .push(`/hobbystory/${storyId}`)
                    } else {
                        alert('스토리 수정에 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (!EditTitle && EditContent && EditFileName) {
            const body = {
                title: StoryContent.title,
                content: EditContent,
                filename: EditFileName
            }

            const headers = {
                Authorization: token
            }

            Axios
                .put(`https://hoppy.kro.kr/api/story?id=${storyId}`, body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('response >>', response)
                    if (response.status === 200) {
                        alert('스토리 수정이 완료되었습니다.')
                        props
                            .history
                            .push(`/hobbystory/${storyId}`)
                    } else {
                        alert('스토리 수정에 실패했습니다. 다시 시도해주세요.')
                    }
                })
        } else if (EditTitle && EditContent && EditFileName) {
            const body = {
                title: EditTitle,
                content: EditContent,
                filename: EditFileName
            }

            const headers = {
                Authorization: token
            }

            Axios
                .put(`https://hoppy.kro.kr/api/story?id=${storyId}`, body, {
                    headers,
                    withCredentials: false
                })
                .then(response => {
                    console.log('response >>', response)
                    if (response.status === 200) {
                        alert('스토리 수정이 완료되었습니다.')
                        props
                            .history
                            .push(`/hobbystory/${storyId}`)
                    } else {
                        alert('스토리 수정에 실패했습니다. 다시 시도해주세요.')
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
                        <p>
                        <Input
                            type='text'
                            placeholder={StoryContent.title}
                            onChange={editTitleHandler}
                            value={EditTitle}
                            style={{
                                width: "350px",
                                height: "46px",
                                background: "#F0F0F0",
                                borderRadius: "14px",
                                margin: "10px 10px 16px 11px",
                                fontSize: "12px"
                            }} />
                            </p>
                        <TextArea
                            type='textarea'
                            placeholder={StoryContent.content}
                            onChange={editContentHandler}
                            value={EditContent}
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
                            maxLength={1000}>
                                </TextArea>
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
                                src={EditFileName?EditFileName:StoryContent.filename}
                                onChange={editFileNameHandler}
                                value={EditFileName}
                                shape="square"
                                size={55} />
                                <p 
                                    style={{
                                        width: '230px', 
                                        marginTop: '12px', 
                                        whiteSpace: 'pre-wrap',
                                        wordBreak: 'break-all'}}>
                                    {Name?Name:imageName}
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
                            수정 완료
                        </Button>
                    </div>
                </Form>
            </div>
        </div>
  )
}

export default EditDetailStoryPage