
# Netty学习项目

一个基于Netty框架的学习示例项目，包含从基础到进阶的多个示例，帮助你理解Netty的核心概念和使用方法。

## 项目简介

本项目是一个基于Spring Boot和Netty 4.1的学习教程项目，通过一系列由浅入深的实例，帮助开发者快速掌握Netty网络编程框架。项目中的每个示例都有详细的注释，便于理解Netty的工作原理和API使用方法。

## 环境要求

- JDK 21
- Maven 3.6+
- Spring Boot 3.4.5
- Netty 4.1.107.Final

## 项目结构

```
src/main/java/com/ince/nettypractise/
├── basic/                 # 基础概念示例
│   ├── discard/           # 丢弃服务器示例
│   ├── echo/              # 回显服务器示例
│   └── time/              # 时间服务器示例
├── codec/                 # 编解码器示例
│   ├── delimiter/         # 分隔符编解码示例
│   ├── fixedlength/       # 定长编解码示例（预留）
│   └── protocol/          # 自定义协议编解码示例（预留）
├── http/                  # HTTP服务器示例
├── websocket/             # WebSocket聊天室示例（预留）
├── heartbeat/             # 心跳检测示例（预留）
├── chat/                  # 完整的聊天应用示例（预留）
├── fileserver/            # 文件传输服务器示例（预留）
└── NettypractiseApplication.java  # 主启动类
```

## 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/inceCheng/nettypractise.git
cd nettypractise
```

### 2. 编译项目
```bash
mvn clean package
```

### 3. 运行项目
```bash
mvn spring-boot:run
```
或者直接运行 `NettypractiseApplication` 类的main方法。

## 运行示例

项目提供了一个交互式菜单，可以选择运行不同的示例：

```
=== Netty 学习示例菜单 ===
1. 丢弃服务器 (基础示例)
2. 回显服务器 (基础示例)
3. 时间服务器 (基础示例)
4. 基于分隔符的服务器 (编解码器示例)
5. HTTP服务器 (协议示例)
0. 退出
请选择要运行的示例:
```

### 运行客户端

对于服务器-客户端示例，需要先启动服务器，然后在单独的终端/进程中启动相应的客户端：

#### 回显服务器/客户端示例：
1. 启动服务器（选项2或直接运行 `EchoServer` 类）
2. 在另一个终端中运行客户端：
   ```
   java -cp target/nettypractise-0.0.1-SNAPSHOT.jar com.ince.nettypractise.basic.echo.EchoClient
   ```

#### 时间服务器/客户端示例：
1. 启动服务器（选项3或直接运行 `TimeServer` 类）
2. 在另一个终端中运行客户端：
   ```
   java -cp target/nettypractise-0.0.1-SNAPSHOT.jar com.ince.nettypractise.basic.time.TimeClient
   ```

#### 分隔符服务器/客户端示例：
1. 启动服务器（选项4或直接运行 `DelimiterBasedServer` 类）
2. 在另一个终端中运行客户端：
   ```
   java -cp target/nettypractise-0.0.1-SNAPSHOT.jar com.ince.nettypractise.codec.delimiter.DelimiterBasedClient
   ```

## 示例说明

### 1. 丢弃服务器 (Discard Server)
最基础的Netty服务器示例，接收客户端的数据但不做任何处理。展示了Netty服务器的基本设置和启动流程。

### 2. 回显服务器 (Echo Server)
接收客户端的数据并原样返回。展示了如何向客户端发送数据以及如何处理客户端请求。

### 3. 时间服务器 (Time Server)
当客户端连接时，服务器主动发送当前时间，然后关闭连接。展示了如何主动向客户端发送数据及处理连接生命周期。

### 4. 基于分隔符的服务器 (Delimiter Based Server)
使用分隔符解码器处理TCP粘包/拆包问题。展示了Netty编解码器的使用方法及如何处理请求/响应消息。

### 5. HTTP服务器 (HTTP Server)
一个简单的HTTP服务器，能够处理HTTP请求并返回HTML响应。展示了如何使用Netty构建Web应用。

## 学习路径

1. 从基础示例开始，理解Netty的核心组件：Channel、ChannelHandler、EventLoop等
2. 学习编解码器的使用，理解如何处理TCP粘包/拆包问题
3. 尝试更复杂的应用场景，如HTTP服务器
4. 探索更高级的主题，如WebSocket、心跳检测等

## 注意事项

- 运行服务器和客户端时需要在不同的终端/进程中
- 确保指定的端口（如8888、8080）未被其他程序占用
- 客户端运行前确保相应的服务器已经启动

## 贡献

欢迎通过Issue和Pull Request贡献代码或提出改进建议。

## 许可证

[MIT License](LICENSE)
