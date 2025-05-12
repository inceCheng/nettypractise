package com.ince.nettypractise.basic.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 时间客户端示例
 * 
 * 连接到时间服务器并接收服务器发送的时间
 * - 展示如何接收服务器主动发送的数据
 * - 展示如何处理服务器之后关闭连接的情况
 */
@Slf4j
public class TimeClient {

    private String host;
    private int port;

    public TimeClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TimeClientHandler());
                    }
                });
            
            // 连接到服务器
            log.info("时间客户端启动中...");
            ChannelFuture f = b.connect(host, port).sync();
            log.info("已连接到时间服务器: {}:{}", host, port);
            
            // 等待连接关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            log.info("时间客户端已关闭");
        }
    }
    
    public static void main(String[] args) throws Exception {
        new TimeClient("localhost", 8888).run();
    }
} 