/* eslint-disable */

import Axios from 'axios'
import React, { useState, useEffect } from 'react'
import {useDispatch} from 'react-redux'
import moment from 'moment'
import { Avatar, Button, Icon, Drawer, Modal } from 'antd'
import {getUser} from '../../../_actions/user_actions'
import './DetailStoryPage.css'
import Like from './Section/Like'

function DetailStoryPage(props) {

    const dispatch = useDispatch();
    const [IsMine, setIsMine] = useState([])

    //drawer 나타내기
    const [visible, setVisible] = useState(false);

    const showDrawer = () => {
        setVisible(true);
    };

    const onClose = () => {
        setVisible(false);
    };

    const storyId = props.match.params.storyId

    const [StoryDetail, setStoryDetail] = useState([])

    const token = localStorage.getItem('Authorization')
    const headers = {
        Authorization: token
    }
    async function getStoryDetail() {
        await Axios
        .get(`https://hoppy.kro.kr/api/story/detail?id=${storyId}`, {
            headers,
            withCredentials: false
        })
        .then(response => {
            if (response.status === 200) {
                setStoryDetail(response.data.data)
            } else {
                alert("스토리를 불러오는데 실패했습니다.")
            }
        })
    }

    useEffect(() => {
      getStoryDetail()
    }, [])

    console.log(StoryDetail)

    //modal 창 관련 event handler
    const [IsModal, setIsModal] = useState(false)

    const showModal = () => {
        setIsModal(true);
    };

    const handleOk = () => {
        setIsModal(false);

        const headers = {
          Authorization: token
        }

        Axios
          .delete(`https://hoppy.kro.kr/api/story?id=${storyId}`, {
            headers,
            withCredentials: false
          })
          .then(response => {
            console.log('res>>>', response)
            if (response.status === 200) {
              alert('스토리를 삭제했습니다.')
              props
                .history
                .push('/hobbystory')
            } else {
              alert('스토리를 삭제하지 못했습니다. 다시 시도해주세요.')
            }
        })
    };

    const handleCancel = () => {
        setIsModal(false);
    };

    //유저 아이디 가져오기

    useEffect(() => {
        dispatch(getUser()).then(response => {
            setIsMine(response.payload.data)
        })
    }, [dispatch, setIsMine])

    console.log('IsMine', IsMine)
    const userId = IsMine.id

    // 작성자 id 값과 현재 로그인 되어 있는 유저의 id 값을 비교하여 예외처리 진행 --
    // 만약 작성자 id 값과 로그인 된 유저의 id 값이 다르면, '신고하기' 버튼이 나오고,
    // 만약 작성자 id 값과 로그인 된 유저의 id 값이 같으면, '수정하기', '삭제하기' 버튼이 나온다.

    //storyDetail
    let createDate = StoryDetail.createdDate + ""
    let datestr = createDate.substr(0, 10)
    let timestr = createDate.substr(11, 15)
    let datemoment = moment(datestr, 'YYYY-MM-DD').add('days').format('MM/DD')
    let timemoment = moment(timestr, 'h:mm::ss').format(' h:mm')
    let date = datemoment + timemoment

    const image = () => {
        if(StoryDetail.filename === '' || StoryDetail.filename === undefined) {
            return <>
                <Avatar shape='square' size={340} style={{display: 'none'}} />
            </>
        } else {
            return <>
                <Avatar shape='square' size={340} style={{marginBottom: '10px'}} src={StoryDetail.filename} />
            </>
        }
    }

    const moreButton = () => {
        if (StoryDetail.memberId === userId) {
            return <>
                <Drawer
                title=""
                placement= 'bottom'
                closable={false}
                onClose={onClose}
                visible={visible}
                height={160}
            >
                <a href={`/hobbystory/${StoryDetail.id}/edit`}>
                    <Button style={{width: '100%', height: '52px', borderRadius: '13px', marginBottom: '7px'}}>
                        수정하기
                    </Button>
                </a>
                <Button style={{width: '100%', height: '52px', borderRadius: '13px'}} onClick={showModal}>
                    삭제하기
                </Button>
                <Modal
                visible={IsModal}
                onOk={handleOk}
                onCancel={handleCancel}
                centered
                width={272}
                footer={[
                    <Button key="exit" onClick={handleOk} style={{ width: '113px', height: '38px', borderRadius: '19px', background: '#D3BA9C'}}>
                      확인
                    </Button>,
                    <Button key="back" onClick={handleCancel} style={{ width: '113px', height: '38px', borderRadius: '19px', background: '#F0F0F0', color: '#888888', marginBottom: '8px'}}>
                        취소
                    </Button>
                ]}>
                  <div style={{ textAlign: 'center' }}>
                    <br />
                    <p>게시글을 삭제하시겠습니까?</p>
                  </div>
            </Modal>
            </Drawer>
            </>
        } else if (StoryDetail.memberId !== userId) {
            return <>
                <Drawer
                title=""
                placement= 'bottom'
                closable={false}
                onClose={onClose}
                visible={visible}
                height={100}
            >
                <a href={`/report`}>
                    <Button style={{width: '100%', height: '52px', borderRadius: '13px', marginBottom: '7px'}}>
                        신고하기
                    </Button>
                </a>
            </Drawer>
            </>
        }
    }

  return (
    <div style={{width: '90%', margin: '0rem auto'}}>
        <hr style={{width: '100%', backgroundColor: '#D3BA9C', border: 0, height: '1px'}} />
        <div style={{width: '100%', display: 'inline-flex', justifyContent: 'flex-start', alignItems: 'stretch', gap: '5px'}}>
            <Avatar size={27} style={{ marginTop: '11px'}} src={StoryDetail.profileUrl}/>
            <p style={{marginTop: '14px'}}>{StoryDetail.username}</p>
            <p style={{fontSize: '8px', marginLeft: 'auto', paddingTop: '13px'}}>{date}</p>
            <Button style={{ border: 0, outline: 0, color: '#000', padding: 0 }} onClick={showDrawer}>
                <Icon type='more' style={{paddingTop: '13px'}} />
            </Button>
            {moreButton()}
        </div>
        <div>
            <div
                style={{
                    display: 'inline-block',
                    width: '100%',
                    whiteSpace: 'pre-wrap',
                    wordBreak: 'break-all',
                }}>
                <p
                    style={{
                        textAlign: 'left',
                        fontSize: '11px',
                    }}>{StoryDetail.title}</p>
                <p
                    style={{
                        textAlign: 'left',
                        fontSize: '9px'
                    }}>{StoryDetail.content}</p>
            </div>
            <div>
                {image()}
            </div>
            <div
                style={{
                    display: 'inline-flex',
                    width: '100%',
                    justifyContent: 'flex-start',
                    alignItems: 'stretch',
                    gap: '7px'
                }}>
                <Icon
                    type='heart'
                    style={{
                        fontSize: '16px',
                        marginTop: '7px'
                    }}/>
                <p style={{marginTop: '4px'}}>
                    {StoryDetail.likeCount}
                </p>
                <Icon
                    type='message'
                    style={{
                        fontSize: '16px',
                        marginTop: '7px'
                    }}/>
                <p style={{marginTop: '4px'}}>
                    {StoryDetail.replyCount}
                </p>
                <div style={{
                    fontSize: '21px',
                    marginLeft: 'auto'
                }}>
                <Like StoryDetail={StoryDetail}/>
                </div>
            </div>
            <hr style={{width: '100%', backgroundColor: '#A5A5A5', border: 0, height: '1px'}} />
        </div>
    </div>
  )
}

export default DetailStoryPage