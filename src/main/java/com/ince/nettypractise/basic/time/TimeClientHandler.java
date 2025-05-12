package com.ince.nettypractise.basic.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 时间客户端处理器
 * 
 * 接收服务器发送的时间信息
 * - 展示如何处理服务器发送的数据
 * - 处理服务器主动关闭连接的情况
 */
@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当从服务器接收到数据时调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        try {
            byte[] bytes = new byte[m.readableBytes()];
            m.readBytes(bytes);
            String timeStr = new String(bytes, StandardCharsets.UTF_8);
            log.info("收到服务器时间: {}", timeStr);
            
            // 注意：服务器会在发送完时间后关闭连接，所以我们不需要主动关闭
        } finally {
            m.release();
        }
    }

    /**
     * 当连接断开时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("服务器已关闭连接");
    }

    /**
     * 当发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("时间客户端发生异常:", cause);
        ctx.close();
    }
} 