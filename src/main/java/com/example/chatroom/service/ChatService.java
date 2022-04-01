package com.example.chatroom.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

public interface ChatService {

    public void register(JSONObject param, ChannelHandlerContext ctx);

    public void singleSend(JSONObject param,ChannelHandlerContext ctx);

    public void groupSend(JSONObject param,ChannelHandlerContext ctx);

    public void remove(ChannelHandlerContext ctx);

    public void typeError(ChannelHandlerContext ctx);

}
