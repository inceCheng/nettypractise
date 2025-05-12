package com.ince.nettypractise.codec.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 基于分隔符的客户端示例
 * 
 * 与分隔符服务器配合使用，发送消息并接收服务器响应
 * - 展示如何在客户端使用编解码器
 * - 提供控制台输入功能让用户发送消息
 */
@Slf4j
public class DelimiterBasedClient {

    private String host;
    private int port;
    
    // 定义分隔符
    private static final String DELIMITER = "_$_";

    public DelimiterBasedClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        
        try {
            Bootstrap b = new Bootstrap();
            
            // 创建分隔符缓冲区
            ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER, StandardCharsets.UTF_8);
            
            b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        // 添加分隔符解码器
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                        
                        // 添加字符串解码器
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        
                        // 添加字符串编码器
                        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                        
                        // 添加处理器
                        ch.pipeline().addLast(new DelimiterBasedClientHandler(DELIMITER));
                    }
                });
            
            // 连接服务器
            log.info("基于分隔符的客户端启动中...");
            ChannelFuture f = b.connect(host, port).sync();
            log.info("已连接到服务器: {}:{}", host, port);
            
            // 获取控制台输入并发送消息
            Scanner scanner = new Scanner(System.in);
            log.info("请输入消息 (输入'exit'退出):");
            
            while (true) {
                String line = scanner.nextLine();
                
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }
                
                // 发送消息到服务器，加上分隔符
                f.channel().writeAndFlush(line + DELIMITER);
                log.info("已发送: {}", line);
            }
            
            // 关闭连接
            f.channel().close().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new DelimiterBasedClient("localhost", 8888).run();
    }
} 