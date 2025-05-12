package com.ince.nettypractise.codec.delimiter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于分隔符的服务器处理器
 * 
 * 处理经过解码器处理后的消息
 * - 注意这里使用的是SimpleChannelInboundHandler而不是ChannelInboundHandlerAdapter
 * - SimpleChannelInboundHandler会自动释放资源，适合用于处理已解码的消息
 */
@Slf4j
public class DelimiterBasedServerHandler extends SimpleChannelInboundHandler<String> {

    private final String delimiter;
    
    public DelimiterBasedServerHandler(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * 从客户端接收到消息时被调用
     * 注意这里的msg已经是String类型，不再是ByteBuf，因为前面有StringDecoder
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("服务器收到消息: {}", msg);
        
        // 构造响应消息，加上分隔符
        String response = "已收到你的消息: " + msg + delimiter;
        
        // 将消息发送回客户端
        // 由于有StringEncoder，我们可以直接发送String类型的消息
        log.info("服务器发送响应: {}", response);
        ctx.writeAndFlush(response);
    }
    
    /**
     * 当新连接建立时被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接建立: {}", ctx.channel().remoteAddress());
        
        // 发送欢迎消息
        String welcome = "欢迎连接到基于分隔符的服务器!" + delimiter;
        ctx.writeAndFlush(welcome);
    }
    
    /**
     * 当连接断开时被调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接断开: {}", ctx.channel().remoteAddress());
    }

    /**
     * 当发生异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("基于分隔符的服务器发生异常:", cause);
        ctx.close();
    }
} 