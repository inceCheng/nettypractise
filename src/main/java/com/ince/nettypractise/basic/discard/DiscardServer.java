package com.ince.nettypractise.basic.discard;

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
 * 丢弃服务器示例 - 丢弃任何收到的数据
 * 
 * Netty基础示例：丢弃服务器
 * - 忽略所有接收的数据
 * - 介绍Netty服务器启动流程
 */
@Slf4j
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 1. 创建两个EventLoopGroup - 这是Netty的线程池
        // bossGroup负责接收客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // workerGroup负责处理客户端连接的数据读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            // 2. 创建ServerBootstrap - 这是Netty服务器的启动引导类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup) // 设置线程组
                .channel(NioServerSocketChannel.class) // 设置通道类型为NIO
                .childHandler(new ChannelInitializer<SocketChannel>() { // 设置处理器
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DiscardServerHandler()); // 添加处理器
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128) // 设置连接队列长度
                .childOption(ChannelOption.SO_KEEPALIVE, true); // 设置保持连接
            
            // 3. 绑定端口并启动服务器
            log.info("丢弃服务器启动中...");
            ChannelFuture f = b.bind(port).sync(); // 绑定端口并同步等待
            log.info("丢弃服务器已启动，监听端口: {}", port);
            
            // 4. 等待服务器Socket关闭
            f.channel().closeFuture().sync();
        } finally {
            // 5. 优雅地关闭线程池
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("丢弃服务器已关闭");
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port = 8888;
        new DiscardServer(port).run();
    }
} 