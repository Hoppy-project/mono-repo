import React from 'react'
import { KAKAO_AUTH_URL, KAKAO_AUTH_URL_LOCAL } from './key'
import Logo from './img/Logo.png'
import kakaoLoginButton from './img/kakaoLoginButton.png'

function KakaoLoginPage() {

    const loginhandler = () => {
        if (window.location.href === 'http://localhost:8888/login') {
            return <>
                <a href={KAKAO_AUTH_URL_LOCAL}>
                    <img src={kakaoLoginButton} alt='img' style={{height: '50%'}}/>
                </a>
            </>
        } else if (window.location.href === 'https://hoppy.kro.kr/login') {
            return <>
                <a href={KAKAO_AUTH_URL}>
                    <img src={kakaoLoginButton} alt='img' style={{height: '50%'}}/>
                </a>
            </>
        }
    }
    return (
        <div style={{textAlign: 'center', marginTop: '180px'}}>
            <img src={Logo} alt='img' style={{width: '217px', height: '109px'}} />
            <h5 style={{fontSize: '11px'}}>지금 바로 Hoppy의 서비스를 시작해보세요!</h5>
            <br />
            {loginhandler()}
        </div>
    )
}

export default KakaoLoginPage