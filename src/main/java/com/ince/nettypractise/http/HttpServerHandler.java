package com.ince.nettypractise.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * HTTP服务器处理器
 * 
 * 处理HTTP请求并返回响应
 * - 展示如何处理HTTP请求
 * - 展示如何构造HTTP响应
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 处理HTTP请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 记录请求信息
        log.info("收到HTTP请求: {} {}", request.method(), request.uri());
        
        // 获取请求URI
        String uri = request.uri();
        
        // 根据URI返回不同的内容
        String responseContent;
        
        if ("/".equals(uri) || "/index.html".equals(uri)) {
            // 主页
            responseContent = createHtmlContent("首页", "欢迎访问Netty HTTP服务器");
        } else if ("/hello".equals(uri)) {
            // Hello页面
            responseContent = createHtmlContent("Hello", "Hello, Netty HTTP Server!");
        } else if ("/time".equals(uri)) {
            // 当前时间
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            responseContent = createHtmlContent("当前时间", "服务器当前时间是: " + currentTime);
        } else {
            // 404页面
            responseContent = createHtmlContent("404 Not Found", "请求的页面不存在");
            sendResponse(ctx, request, NOT_FOUND, responseContent);
            return;
        }
        
        // 发送正常响应
        sendResponse(ctx, request, OK, responseContent);
    }
    
    /**
     * 创建HTML内容
     */
    private String createHtmlContent(String title, String content) {
        return "<!DOCTYPE html>\r\n" +
               "<html>\r\n" +
               "<head>\r\n" +
               "    <meta charset=\"UTF-8\">\r\n" +
               "    <title>" + title + "</title>\r\n" +
               "    <style>\r\n" +
               "        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }\r\n" +
               "        h1 { color: #333; }\r\n" +
               "        p { color: #666; }\r\n" +
               "        .container { max-width: 800px; margin: 0 auto; }\r\n" +
               "        nav { margin-top: 20px; }\r\n" +
               "        nav a { margin-right: 10px; }\r\n" +
               "    </style>\r\n" +
               "</head>\r\n" +
               "<body>\r\n" +
               "    <div class=\"container\">\r\n" +
               "        <h1>" + title + "</h1>\r\n" +
               "        <p>" + content + "</p>\r\n" +
               "        <nav>\r\n" +
               "            <a href=\"/\">首页</a>\r\n" +
               "            <a href=\"/hello\">Hello</a>\r\n" +
               "            <a href=\"/time\">当前时间</a>\r\n" +
               "        </nav>\r\n" +
               "    </div>\r\n" +
               "</body>\r\n" +
               "</html>\r\n";
    }
    
    /**
     * 发送HTTP响应
     */
    private void sendResponse(ChannelHandlerContext ctx, FullHttpRequest request, HttpResponseStatus status, String content) {
        // 创建响应
        ByteBuf byteBuf = Unpooled.copiedBuffer(content, StandardCharsets.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);
        
        // 设置响应头
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
        
        // 如果是保持连接
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        } else {
            // 如果不是保持连接，写入后关闭连接
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
    
    /**
     * 当发生异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("HTTP服务器处理器发生异常:", cause);
        ctx.close();
    }
} 