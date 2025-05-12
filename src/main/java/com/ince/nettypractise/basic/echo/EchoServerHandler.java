package com.ince.nettypractise.basic.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 回显服务器处理器
 * 
 * 处理客户端发送的数据，并将相同的数据发送回客户端
 * - 展示如何读取客户端数据
 * - 展示如何向客户端发送数据
 */
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 每当从客户端收到新的数据时，这个方法会被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 获取客户端发送的数据
        ByteBuf in = (ByteBuf) msg;
        
        try {
            // 打印接收到的数据
            log.info("回显服务器收到消息:");
            StringBuilder sb = new StringBuilder();
            while (in.isReadable()) {
                char c = (char) in.readByte();
                sb.append(c);
                System.out.print(c);
                System.out.flush();
            }
            System.out.println();
            
            // 重要：将数据回显给客户端
            // 要重新创建一个ByteBuf，因为之前的已读取完毕
            ByteBuf out = ctx.alloc().buffer();
            out.writeBytes(sb.toString().getBytes());
            
            log.info("回显服务器发送消息: {}", sb.toString());
            // 将数据写入到客户端
            ctx.writeAndFlush(out);
        } finally {
            // 注意：这里不需要手动释放in（msg）
            // 因为Netty会在channelReadComplete中为我们处理
        }
    }

    /**
     * 当发生异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常时关闭连接
        log.error("回显服务器发生异常:", cause);
        ctx.close();
    }
} 