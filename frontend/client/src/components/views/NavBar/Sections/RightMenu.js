import React from 'react';
import { Menu } from 'antd';
import { withRouter } from 'react-router-dom';;

function RightMenu(props) {

    if (localStorage.Authorization === 'Bearer null' || localStorage.Authorization === undefined) {
      return (
        <Menu mode={props.mode}>
          <Menu.Item key="login">
            <a href="/login">로그인 ▷</a>
          </Menu.Item>
        </Menu>
      )
    } else {
        return (
          <Menu mode={props.mode}>
            <Menu.Item key="logout">
              <a href='/auth/logout'>
                로그아웃 ▷
              </a>
            </Menu.Item>
          </Menu>
        )
    }
}

export default withRouter(RightMenu);

