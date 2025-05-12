package com.ince.nettypractise;

import com.ince.nettypractise.basic.discard.DiscardServer;
import com.ince.nettypractise.basic.echo.EchoServer;
import com.ince.nettypractise.basic.time.TimeServer;
import com.ince.nettypractise.codec.delimiter.DelimiterBasedServer;
import com.ince.nettypractise.http.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * Netty学习项目主启动类
 *
 * 提供控制台菜单选择不同的Netty示例进行运行
 */
@SpringBootApplication
@Slf4j
public class NettypractiseApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettypractiseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        showMenu();
    }

    /**
     * 显示菜单并处理用户选择
     */
    private void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            log.info("=== Netty 学习示例菜单 ===");
            log.info("1. 丢弃服务器 (基础示例)");
            log.info("2. 回显服务器 (基础示例)");
            log.info("3. 时间服务器 (基础示例)");
            log.info("4. 基于分隔符的服务器 (编解码器示例)");
            log.info("5. HTTP服务器 (协议示例)");
            log.info("0. 退出");
            log.info("请选择要运行的示例: ");

            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 0:
                        log.info("退出程序...");
                        return;
                    case 1:
                        log.info("启动丢弃服务器...");
                        new DiscardServer(8888).run();
                        break;
                    case 2:
                        log.info("启动回显服务器...");
                        new EchoServer(8888).run();
                        break;
                    case 3:
                        log.info("启动时间服务器...");
                        new TimeServer(8888).run();
                        break;
                    case 4:
                        log.info("启动基于分隔符的服务器...");
                        new DelimiterBasedServer(8888).run();
                        break;
                    case 5:
                        log.info("启动HTTP服务器...");
                        new HttpServer(8080).run();
                        break;
                    default:
                        log.info("无效的选择，请重试");
                }
            } catch (Exception e) {
                log.error("启动服务器时发生错误: ", e);
            }
        }
    }
}