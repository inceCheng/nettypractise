package com.ince.nettypractise.codec.delimiter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于分隔符的客户端处理器
 * 
 * 处理服务器返回的已解码消息
 * - 使用SimpleChannelInboundHandler处理String类型的消息
 * - 展示如何处理接收到的数据
 */
@Slf4j
public class DelimiterBasedClientHandler extends SimpleChannelInboundHandler<String> {

    private final String delimiter;
    
    public DelimiterBasedClientHandler(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * 从服务器接收到消息时被调用
     * 由于使用了StringDecoder，这里直接处理String类型
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 接收服务器返回的消息，注意这里的消息已经不包含分隔符了，分隔符被解码器处理掉了
        log.info("客户端收到消息: {}", msg);
    }

    /**
     * 当连接建立时被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("已连接到服务器");
    }

    /**
     * 当连接断开时被调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与服务器的连接已断开");
    }

    /**
     * 当发生异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端发生异常:", cause);
        ctx.close();
    }
} 