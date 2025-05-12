package com.ince.nettypractise.basic.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间服务器处理器
 * 
 * 当建立连接时，主动向客户端发送当前时间，然后关闭连接
 * - 展示如何使用channelActive事件
 * - 展示如何在发送完数据后关闭连接
 */
@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当连接建立时被调用
     * 这里不同于前面的例子，我们不需要等待客户端发送数据，而是主动发送
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // 创建ByteBuf来存放要发送的消息
        ByteBuf time = ctx.alloc().buffer(64);
        
        // 获取当前时间并格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = "Current time: " + sdf.format(new Date());
        
        // 将时间字符串写入ByteBuf
        time.writeBytes(timeStr.getBytes());
        
        // 发送时间信息
        log.info("发送时间: {}", timeStr);
        
        // 发送消息
        final ChannelFuture f = ctx.writeAndFlush(time);
        
        // 添加监听器在发送完成后关闭连接
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                log.info("发送完成，关闭连接");
                ctx.close();
            }
        });
    }

    /**
     * 当发生异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("时间服务器发生异常:", cause);
        ctx.close();
    }
} 