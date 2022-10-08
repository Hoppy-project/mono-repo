import React from 'react'

import ArtImg from '../LandingPage/img/art.png'
import DailyImg from '../LandingPage/img/daily.png'
import ExerciseImg from '../LandingPage/img/exercise.png'
import FoodImg from '../LandingPage/img/food.png'
import MusicImg from '../LandingPage/img/music.png'
import TripImg from '../LandingPage/img/trip.png'

function MakeMeetingPage() {

    const hobbyBoxStyle = {
        width: '108px',
        height: '108px',
        border: 'solid 0.8px #A5A5A5',
        borderRadius: '50px',
        display: 'inline-block',
        margin: '7px',
        padding: '10px'
    }

    const hobbyImageStyle = {
        width: '62px',
        height: '62px'
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
                <h3 style={{textAlign: 'left', marginLeft: '27px'}}>모임 카테고리 선택</h3>
                <p style={{textAlign: 'left', marginLeft: '27px', fontSize:'12px', marginBottom: '30px', color: '#858585'}}>
                  만드시려는 모임에 해당하는 카테고리를 하나 선택해주세요.</p>
                <a
                    href='/makeMeeting/exercise'
                    style={{
                        textDecoration: 'none',
                        color: '#000'
                    }}>
                    <div style={hobbyBoxStyle}>
                        운동
                        <img src={ExerciseImg} alt='exercise' style={hobbyImageStyle}/>
                    </div>
                </a>
                <a
                    href='/makeMeeting/art'
                    style={{
                        textDecoration: 'none',
                        color: '#000'
                    }}>
                    <div style={hobbyBoxStyle}>
                        공예
                        <img src={ArtImg} alt='art' style={hobbyImageStyle}/>
                    </div>
                </a>
                <a
                    href='/makeMeeting/music'
                    style={{
                        textDecoration: 'none',
                        color: '#000'
                    }}>
                    <div style={hobbyBoxStyle}>
                        음악
                        <img src={MusicImg} alt='music' style={hobbyImageStyle}/>
                    </div>
                </a>
                <a
                    href='/makeMeeting/daily'
                    style={{
                        textDecoration: 'none',
                        color: '#000'
                    }}>
                    <div style={hobbyBoxStyle}>
                        일상
                        <img src={DailyImg} alt='daily' style={hobbyImageStyle}/>
                    </div>
                </a>
                <a
                    href='/makeMeeting/food'
                    style={{
                        textDecoration: 'none',
                        color: '#000'
                    }}>
                    <div style={hobbyBoxStyle}>
                        음식
                        <img src={FoodImg} alt='food' style={hobbyImageStyle}/>
                    </div>
                </a>
                <a
                    href='/makeMeeting/trip'
                    style={{
                        textDecoration: 'none',
                        color: '#000'
                    }}>
                    <div style={hobbyBoxStyle}>
                        여행
                        <img src={TripImg} alt='trip' style={hobbyImageStyle}/>
                    </div>
                </a>
            </div>
        </div>
    )
}

export default MakeMeetingPage