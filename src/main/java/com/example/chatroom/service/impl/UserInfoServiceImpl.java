package com.example.chatroom.service.impl;

import com.example.chatroom.dao.UserInfoDao;
import com.example.chatroom.model.po.UserInfo;
import com.example.chatroom.model.vo.ResponseJson;
import com.example.chatroom.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserInfoServiceImpl implements UserInfoService {


    private UserInfoDao userInfoDao;


    @Override
    public ResponseJson getByUserId(String userId) {
        UserInfo userInfo = userInfoDao.getByUserId(userId);
        return new ResponseJson().success().setData("userInfo",userInfo);
    }
}
