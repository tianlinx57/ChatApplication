package fr.utc.sr03.chat.websocket_netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;

public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    public static final AttributeKey<String> WEBSOCKET_PATH = AttributeKey.valueOf("WEBSOCKET_PATH");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(1);
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;

            // 提取 URI 并存储在 Channel 的 Attribute 中
            String uri = request.uri();
            ctx.channel().attr(WEBSOCKET_PATH).set(uri);
            System.out.println("URI extracted: " + uri);
        }

        // 传递消息给下一个处理器
        ctx.fireChannelRead(msg);
    }
}
