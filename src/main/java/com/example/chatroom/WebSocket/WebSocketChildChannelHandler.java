package com.example.chatroom.WebSocket;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebSocketChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Resource(name = "webSocketServerHandler")
    private ChannelHandler webSocketServerHandler;

    @Resource(name = "httpRequestHandler")
    private ChannelHandler httpRequsetHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("http-codec",new HttpServerCodec()); // HTTP编解码器
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//把HTTP头、HTTP体拼成完整的HTTP请求
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//方便大文件传输，不过实质上都是短的文本数据
        ch.pipeline().addLast("http-handler",httpRequsetHandler);
        ch.pipeline().addLast("websocket-handler",webSocketServerHandler);
    }
}
