package fr.utc.sr03.chat.websocket_netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatWebSocketInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // 配置 HTTP 编解码器
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());

        // 添加自定义 HttpRequestHandler，用于提取 WebSocket 握手请求路径
        pipeline.addLast(new HttpRequestHandler());

        // 配置 WebSocket 协议处理器，设置路径为 /websocket/{chatId}/{email}
        pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));

        // 添加 WebSocket 处理器
        pipeline.addLast(new ChatWebSocketHandler());
    }
}
