import {Avatar, Button, Icon, Tabs} from 'antd'
import React, { useEffect, useState } from 'react'
import { useDispatch } from 'react-redux';
import './MyPage.css'
import { getUser } from '../../../_actions/user_actions';

const {TabPane} = Tabs;

function MyPage() {

    const dispatch = useDispatch();
    const [UserInfo, setUserInfo] = useState([])

    const token = localStorage.getItem('Authorization')
    console.log('token', token)

    useEffect(() => {
      dispatch(getUser()).then(response => {
        console.log('res>>>', response)
        setUserInfo(response.payload.data)
      })
    }, [dispatch, setUserInfo])
    
    const Intro = UserInfo.intro
    const ProfileUrl = UserInfo.profileUrl
    const UserName = UserInfo.username

    const onChange = (key) => {
        // console.log(key);
    };

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
                <div>
                    <p
                        style={{
                            textAlign: 'left',
                            marginLeft: '27px',
                            fontSize: '16px',
                            display: 'inline-block',
                            float: 'left'
                        }}>마이페이지</p>
                    <a href='/mypage/edit'>
                        <Button
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
                            프로필 수정
                        </Button>
                    </a>
                </div>
                <Avatar
                    src={ProfileUrl}
                    style={{
                        width: '142px',
                        height: '142px',
                        marginTop: '48px',
                        marginRight: '10px',
                        background: '#A5A5A5',
                        textAlign: 'center'
                    }}
                />
                <p style={{
                        marginTop: '16px',
                        textAlign: 'center'
                    }}>반가워요 {UserName}님!</p>
                <p
                    style={{
                        textAlign: 'left',
                        margin: '30px 0px 0px 27px',
                        fontSize: '12px',
                        color: '#858585'
                    }}>다른 사람들에게 자신을 소개해주세요!</p>
                <div
                    style={{
                        width: '90%',
                        height: '117px',
                        margin: '18px 20px 0px 20px',
                        padding: '20px 24px 20px 24px',
                        fontSize: '11px',
                        color: '#464646',
                        border: '0.8px solid #A5A5A5',
                        borderRadius: '10px'
                    }}>
                    <div
                        style={{
                            display: 'block',
                            width: '100%',
                            height: '100%',
                            textAlign: 'left',
                            whiteSpace: 'pre-wrap',
                            wordBreak: 'break-all',
                            overflowY: 'auto'
                        }}>
                        {Intro}
                    </div>
                </div>
                <div>
                    <p
                        style={{
                            margin: '36px 0px 0px 30px',
                            fontSize: '19px',
                            textAlign: 'left'
                        }}>모임</p>
                    <Tabs
                        defaultActiveKey="1"
                        centered="centered"
                        onChange={onChange}
                        style={{
                            primaryColor: '#D3BA9C'
                        }}>
                        <TabPane tab="나의 모임" key="1">
                            나의 모임
                        </TabPane>
                        <TabPane tab="관심 모임" key="2">
                            관심 모임
                        </TabPane>
                    </Tabs>
                </div>
                <p
                    style={{
                        margin: '36px 0px 0px 30px',
                        fontSize: '19px',
                        textAlign: 'left'
                    }}>취미 스토리</p>
                <a href='/mypage/mystory'>
                    <div
                        style={{
                            color: '#858585',
                            marginLeft: '30px',
                            textAlign: 'left'
                        }}>
                        <p
                            style={{
                                margin: '18px 0px 0px 0px',
                                fontSize: '12px',
                                display: 'inline-block'
                            }}>나의 스토리</p>
                        <Icon
                            type='right'
                            style={{
                                marginLeft: '235px'
                            }}/>
                        <hr
                            style={{
                                width: '90%',
                                float: 'left',
                                display: 'inline-block'
                            }}/>
                    </div>
                </a>
            </div>
        </div>
    )
}

export default MyPage