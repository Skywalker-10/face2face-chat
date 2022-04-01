package com.example.chatroom.WebSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：Netty WebSocket服务器 使用独立的线程启动
 */

@Service
public class WebSocketServer implements Runnable{




    private EventLoopGroup bossGroup;


    private  EventLoopGroup workerGroup;


    private ServerBootstrap serverBootstrap;

//    private int port;
    private ChannelHandler childchannelHandler;
    private ChannelFuture serverChannelFuture;

    public WebSocketServer(){}





    @Override
    public void run() {
        build();
    }

    public void build(){
        try {
            long begin = System.currentTimeMillis();
            serverBootstrap.group(bossGroup,workerGroup) // 主从线程模型 boss辅助客户端进行连接，worker负责与客户端之间的读写
                    .channel(NioServerSocketChannel.class) // 配置客户端的channel类型
                    .option(ChannelOption.SO_BACKLOG,1024) // 配置TCP参数，握手字符串长度设置
                    .option(ChannelOption.TCP_NODELAY,true) // TCP_NOODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                    .childOption(ChannelOption.SO_KEEPALIVE,true) // 开启心跳包活机制，就是客户端、服务端处于ESTABLISH状态，超过2小时没有交流，机制会被启动
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR,new FixedRecvByteBufAllocator(592048)) // 配置固定长度接受缓存区分配器
                    .childHandler(childchannelHandler); //绑定I/O时间的处理类，WebSocketChildChannelHandler中定义

            long end = System.currentTimeMillis();
            System.out.println("Netty WebSocket服务器启动完成耗时"+(end-begin)+"ms，已绑定端口"+3333+"阻塞式等候客户端连接");

            serverChannelFuture = serverBootstrap.bind(3333).sync();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }

    }

    /**
     * 描述：关闭Netty WebSocket服务器，主要是释放连接
     *     连接包括：服务器连接severChannel，
     *     客户端TCP处理连接bossGroup，
     *     客户端I/O操作连接workerGroup
     *
     *     若只使用
     *        bossGroupFuture = bossGroup.shutdownGracefully();
     *        workerGroupFuture = workerGroup.shutdownGracefully();
     *     会造成内存泄漏。
     *
     */

    public void close(){
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();

        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
    }

    public ChannelHandler getChildChannelHandler(){return childchannelHandler;}

    public void setChildchannelHandler(ChannelHandler channelHandler){
        this.childchannelHandler = channelHandler;
    }

    public int getPort(){return 3333;}

//    public void setPort(int port){
//        this. = port;
//    }
}
