package com.ince.nettypractise.basic.echo;

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
 * 回显客户端示例
 *
 * 与回显服务器配合使用，发送消息并接收服务器回显的数据
 * - 展示Netty客户端的基本工作流程
 * - 展示Bootstrap的使用方法
 */
@Slf4j
public class EchoClient {

    private String host;
    private int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        // 1. 创建一个EventLoopGroup（客户端只需要一个线程组）
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 2. 创建Bootstrap（客户端使用Bootstrap而不是ServerBootstrap）
            Bootstrap b = new Bootstrap();
            b.group(workerGroup) // 设置线程组
                .channel(NioSocketChannel.class) // 客户端使用NioSocketChannel
                .option(ChannelOption.SO_KEEPALIVE, true) // 设置保持连接
                .handler(new ChannelInitializer<SocketChannel>() { // 设置处理器
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoClientHandler());
                    }
                });

            // 3. 连接到服务器
            log.info("回显客户端启动中...");
            // 连接到指定的主机和端口
            ChannelFuture f = b.connect(host, port).sync();
            log.info("已连接到服务器: {}:{}", host, port);

            // 4. 等待连接关闭
            f.channel().closeFuture().sync();
        } finally {
            // 5. 优雅地关闭线程池
            workerGroup.shutdownGracefully();
            log.info("回显客户端已关闭");
        }
    }

    public static void main(String[] args) throws Exception {
        // 使用 localhost 和 8888 端口进行测试
        new EchoClient("localhost", 8888).run();
    }
} 