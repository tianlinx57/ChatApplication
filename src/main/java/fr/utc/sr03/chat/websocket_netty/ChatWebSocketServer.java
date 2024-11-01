package fr.utc.sr03.chat.websocket_netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import javax.annotation.PreDestroy;

@Component
public class ChatWebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketServer.class);
    private final int port = 8999; // 端口号
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        Thread thread = new Thread(() -> {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();

            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(
                            //     new ChannelInitializer<Channel>() {
                            //     @Override
                            //     protected void initChannel(Channel ch) throws Exception {
                            //         ChannelPipeline pipeline = ch.pipeline();
                            //         pipeline.addLast(new HttpServerCodec());
                            //         pipeline.addLast(new HttpObjectAggregator(65536));
                            //         pipeline.addLast(new ChunkedWriteHandler());
                            //         pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
                            //         pipeline.addLast(new ChatWebSocketHandler());
                            //     }
                            // }
                                new ChatWebSocketInitializer()
                        );
                channel = b.bind(port).sync().channel();
                logger.info("WebSocket server started on ws://localhost:{}", port);
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Netty WebSocket server interrupted", e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
        thread.setDaemon(true); // 设置为守护线程
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        // new ChatWebSocketServer(8080).start();
    }
}
