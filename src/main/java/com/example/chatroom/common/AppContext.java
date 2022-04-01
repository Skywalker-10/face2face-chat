package com.example.chatroom.common;


import com.example.chatroom.WebSocket.WebSocketServer;
import com.example.chatroom.dao.GroupInfoDao;
import com.example.chatroom.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Scope("singleton")
public class AppContext {

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private GroupInfoDao groupInfoDao;

    private Thread nettyThread;

    /**
     *
     */

    @PostConstruct
    public void init(){
        nettyThread = new Thread(webSocketServer);
        System.out.println("开启独立线程，启动netty WebSocket服务器。。。");
        nettyThread.start();
        System.out.println("加载用户数据。。。");
        userInfoDao.loadUserInfo();
        System.out.println("加载用户交流群数据。。。");
        groupInfoDao.loadGroupInfo();
    }


    @PreDestroy
    public void close(){
        System.out.println("正在释放Netty WebSocket相关连接。。。");
        webSocketServer.close();
        System.out.println("正在关闭Netty Websocket服务器线程。。。");
        nettyThread.stop();
        System.out.println("系统关闭成功！");
    }


}
