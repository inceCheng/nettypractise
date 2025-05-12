package com.ince.nettypractise.basic.echo;

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
 * 回显服务器示例 - 将接收到的数据发送回客户端
 * 
 * 这是Netty入门的第二个示例，与丢弃服务器类似，但会将接收到的数据回显给客户端
 * - 展示如何响应客户端请求
 * - 展示Netty中数据的发送方法
 */
@Slf4j
public class EchoServer {

    private int port;

    public EchoServer(int port) {
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
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            // 3. 绑定端口并启动服务器
            log.info("回显服务器启动中...");
            ChannelFuture f = b.bind(port).sync();
            log.info("回显服务器已启动，监听端口: {}", port);
            
            // 4. 等待服务器Socket关闭
            f.channel().closeFuture().sync();
        } finally {
            // 5. 优雅地关闭线程池
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("回显服务器已关闭");
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port = 8888;
        new EchoServer(port).run();
    }
} 