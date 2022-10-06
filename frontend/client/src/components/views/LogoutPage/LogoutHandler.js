import React from 'react'

function LogoutHandler() {

    const logoutHandler = () => {
        if (window.location.href === 'http://localhost:8888/auth/logout') {
            return window.location.href='https://kauth.kakao.com/oauth/logout?client_id=e1ec33bfac22318d6629869f6bf7bc7a&logout_redirect_uri=http://localhost:8888/logout'
        } else if (window.location.href === 'https://hoppy.kro.kr/auth/logout') {
            return window.location.href='https://kauth.kakao.com/oauth/logout?client_id=e1ec33bfac22318d6629869f6bf7bc7a&logout_redirect_uri=https://hoppy.kro.kr/logout'
        }
    }

  return (
    <div>
        {logoutHandler()}
    </div>
  )
}

export default LogoutHandler