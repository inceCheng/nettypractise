package com.ince.nettypractise.basic.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 时间服务器示例 - 当客户端连接时发送当前时间，然后关闭连接
 * 
 * 这是Netty入门的第三个示例
 * - 展示如何主动发送数据给客户端
 * - 展示如何在特定事件（连接建立）上触发操作
 */
@Slf4j
public class TimeServer {

    private int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 1. 创建两个EventLoopGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            // 2. 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TimeServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            // 3. 绑定端口并启动服务器
            log.info("时间服务器启动中...");
            ChannelFuture f = b.bind(port).sync();
            log.info("时间服务器已启动，监听端口: {}", port);
            
            // 4. 等待服务器Socket关闭
            f.channel().closeFuture().sync();
        } finally {
            // 5. 优雅地关闭线程池
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("时间服务器已关闭");
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port = 8888;
        new TimeServer(port).run();
    }
} 