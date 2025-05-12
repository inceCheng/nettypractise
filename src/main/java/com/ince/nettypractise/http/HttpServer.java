package com.ince.nettypractise.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP服务器示例
 * 
 * 展示如何使用Netty创建一个简单的HTTP服务器
 * - 使用HTTP相关的编解码器
 * - 处理HTTP请求并返回响应
 */
@Slf4j
public class HttpServer {

    private int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        // HTTP编解码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        
                        // 将多个消息转换为单一的FullHttpRequest或FullHttpResponse
                        ch.pipeline().addLast(new HttpObjectAggregator(65536));
                        
                        // 用于大数据流的分块传输
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        
                        // 我们自定义的处理器
                        ch.pipeline().addLast(new HttpServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            log.info("HTTP服务器启动中...");
            ChannelFuture f = b.bind(port).sync();
            log.info("HTTP服务器已启动，监听端口: {}", port);
            
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("HTTP服务器已关闭");
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port = 8080;
        new HttpServer(port).run();
    }
} 