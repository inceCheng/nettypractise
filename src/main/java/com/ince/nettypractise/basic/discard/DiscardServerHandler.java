package com.ince.nettypractise.basic.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 丢弃服务器处理器
 * 
 * 处理客户端发送的数据，在这个示例中简单地丢弃接收到的数据
 * - 展示ChannelHandler的基本使用方法
 * - 展示如何处理接收到的数据
 */
@Slf4j
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 每当从客户端收到新的数据时，这个方法会被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 丢弃接收到的数据
        ByteBuf in = (ByteBuf) msg;
        try {
            // 打印接收到的数据内容 (非必须，只是为了演示)
            log.info("丢弃服务器收到消息:");
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
            System.out.println();
            
            // 由于是丢弃服务器，我们实际上什么都不做，只是打印
            // 在实际应用中，这里会有业务逻辑处理
        } finally {
            // 释放消息资源。Netty使用引用计数来处理池化的ByteBuf
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 当发生异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常时关闭连接
        log.error("丢弃服务器发生异常:", cause);
        ctx.close();
    }
} 