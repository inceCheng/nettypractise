package com.ince.nettypractise.codec.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 基于分隔符的服务器示例
 * 
 * 使用DelimiterBasedFrameDecoder解码器处理TCP粘包/拆包问题
 * - 展示如何使用编解码器
 * - 解决TCP粘包/拆包问题
 */
@Slf4j
public class DelimiterBasedServer {

    private int port;
    
    // 定义分隔符
    private static final String DELIMITER = "_$_";

    public DelimiterBasedServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            
            // 创建分隔符缓冲区
            ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER, StandardCharsets.UTF_8);
            
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        // 添加分隔符解码器 - 最大帧长度为1024字节，分隔符为_$_
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                        
                        // 添加字符串解码器 - 将ByteBuf转换成String
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        
                        // 添加字符串编码器 - 将String转换成ByteBuf
                        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                        
                        // 添加我们自己的处理器
                        ch.pipeline().addLast(new DelimiterBasedServerHandler(DELIMITER));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            log.info("基于分隔符的服务器启动中...");
            ChannelFuture f = b.bind(port).sync();
            log.info("基于分隔符的服务器已启动，监听端口: {}", port);
            
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("基于分隔符的服务器已关闭");
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port = 8888;
        new DelimiterBasedServer(port).run();
    }
} 