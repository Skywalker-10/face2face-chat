package com.example.chatroom.WebSocket;

import com.alibaba.fastjson.JSONObject;
import com.example.chatroom.model.vo.ResponseJson;
import com.example.chatroom.service.ChatService;
import com.example.chatroom.util.Constant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {



    @Autowired
    private ChatService chatService;

    /**
     * 描述：读取完连接的信息后，对消息进行处理
     *     这里主要是处理WebSocket请求
     */

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handlerWebSocketFrame(ctx,msg);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame) throws Exception{
        // 关闭请求
        if (frame instanceof CloseWebSocketFrame){
            WebSocketServerHandshaker handshaker = Constant.webSocketServerHandshakerMap.get(ctx.channel().id().asLongText());
            if (handshaker == null){
                sendErrorMessage(ctx,"不存在的客户端连接！");
            }
            else{
                handshaker.close(ctx.channel(),(CloseWebSocketFrame)frame.retain() );
            }
            return;

        }
        // ping请求
        if (frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)){
            sendErrorMessage(ctx,"仅支持文本（Text）格式，不支持二进制消息");
        }

        // 客户端发送过来的信息
        String request = ((TextWebSocketFrame)frame).text();
        System.out.println("服务端收到新消息："+request);
        JSONObject param = null;
        try{
            param = JSONObject.parseObject(request);
        }catch (Exception e){
            sendErrorMessage(ctx,"JSON字符串转换出错!");
            e.printStackTrace();
        }
        if (param == null){
            sendErrorMessage(ctx,"参数为空");
            return;
        }

        String type = (String) param.get("type");
        switch (type){
            case "REGISTER":
                chatService.register(param,ctx);
                break;
            case "SINGLE_SENDING":
                chatService.singleSend(param,ctx);
                break;
            case "GROUP_SENDING":
                chatService.groupSend(param,ctx);
                break;
            default:
                chatService.typeError(ctx);
                break;
        }

    }
    /**
     * 描述：客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        chatService.remove(ctx);
    }

    /**
     * 异常处理：关闭channel
     */
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        cause.printStackTrace();;
        ctx.close();
    }

    private void sendErrorMessage(ChannelHandlerContext ctx,String errorMsg){
        String reponseJson = new ResponseJson().error(errorMsg).toString();

        ctx.channel().writeAndFlush(new TextWebSocketFrame(reponseJson));
    }


}
