package com.example.chatroom.service.impl;

import com.example.chatroom.dao.UserInfoDao;
import com.example.chatroom.model.po.UserInfo;
import com.example.chatroom.model.vo.ResponseJson;
import com.example.chatroom.service.SecurityService;
import com.example.chatroom.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

public class SecurityServiceImpl implements SecurityService {


    private UserInfoDao userInfoDao;



    @Override
    public ResponseJson login(String username, String password, HttpSession session) {
        UserInfo userInfo = userInfoDao.getByUsername(username);
        if (userInfo == null){
            return new ResponseJson().error("不存在该用户名");
        }
        if (!userInfo.getPassword().equals(password)){
            return new ResponseJson().error("密码不正确");
        }
        session.setAttribute(Constant.USER_TOKEN,userInfo.getUserId());
        return new ResponseJson().success();

    }

    @Override
    public ResponseJson logout(HttpSession session) {
        Object userId = session.getAttribute(Constant.USER_TOKEN);
        if (userId == null){
            return new ResponseJson().error("请先登录！");
        }
        session.removeAttribute(Constant.USER_TOKEN);
        System.out.println(MessageFormat.format("userId为 {0} 的用户已注销登录！",userId));
        return new ResponseJson().success();
    }
}
