package com.ince.nettypractise.basic.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 回显客户端处理器
 *
 * 处理客户端连接和数据交互，发送消息并处理服务器响应
 * - 展示如何在客户端发送消息
 * - 展示如何处理服务器响应
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;

    /**
     * 构造函数，创建要发送的消息
     */
    public EchoClientHandler() {
        String message = "Hello, Netty Echo Server!";
        // 创建一个ByteBuf，写入要发送的内容
        firstMessage = Unpooled.buffer(message.length());
        firstMessage.writeBytes(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 当客户端连接服务器完成时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 连接建立时发送消息
        log.info("客户端连接建立，发送消息: {}", new String(firstMessage.array(), StandardCharsets.UTF_8));
        // 写入并刷新到服务器
        ctx.writeAndFlush(firstMessage.copy());
    }

    /**
     * 当客户端收到服务器的消息时被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        
        try {
            while (in.isReadable()) {
                sb.append((char) in.readByte());
            }
            String response = sb.toString();
            log.info("客户端收到服务器响应: {}", response);

            // 收到响应后关闭连接
            ctx.close();
        } finally {
            // 释放消息资源
            in.release();
        }
    }

    /**
     * 当发生异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常时关闭连接
        log.error("客户端发生异常:", cause);
        ctx.close();
    }
} 