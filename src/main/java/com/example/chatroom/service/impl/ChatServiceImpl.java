package com.example.chatroom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.chatroom.service.ChatService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {




    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {

    }

    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) {

    }

    @Override
    public void groupSend(JSONObject param, ChannelHandlerContext ctx) {

    }

    @Override
    public void remove(ChannelHandlerContext ctx) {

    }

    @Override
    public void typeError(ChannelHandlerContext ctx) {

    }
}
