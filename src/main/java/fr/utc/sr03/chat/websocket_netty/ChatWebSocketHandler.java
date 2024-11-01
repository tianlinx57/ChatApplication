package fr.utc.sr03.chat.websocket_netty;

import com.google.gson.Gson;
import fr.utc.sr03.chat.websocket.ChatMessage;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private static final Gson gson = new Gson();
    private static final Map<Long, Map<String, Channel>> CHATS = new ConcurrentHashMap<>();

    private Long chatId;
    private String email;

    // 使用正则表达式来提取 URL 中的 chatId 和 email
    private static final Pattern PATH_PATTERN = Pattern.compile("/websocket/(\\d+)/(\\S+)");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();

        // 从 Attribute 中获取存储的 URI
        String uri = channel.attr(HttpRequestHandler.WEBSOCKET_PATH).get();

        System.out.println(uri);

        // 使用正则表达式解析 chatId 和 email
        Matcher matcher = PATH_PATTERN.matcher(uri);
        if (matcher.matches()) {
            this.chatId = Long.parseLong(matcher.group(1));
            this.email = matcher.group(2);
            logger.info("用户 {} 已加入聊天 {}", email, chatId);

            Map<String, Channel> chat = CHATS.computeIfAbsent(chatId, k -> new ConcurrentHashMap<>());
            chat.put(email, channel);

            // 广播加入信息
            ChatMessage joinMessage = new ChatMessage(email, "joined the chat!", Instant.now().getEpochSecond());
            broadcastMessage(chat, joinMessage);
        } else {
            logger.warn("URL 未匹配预期模式，连接将被关闭: {}", uri);
            ctx.close();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Map<String, Channel> chat = CHATS.get(chatId);

        if (chat != null) {
            chat.remove(email);
            ChatMessage leaveMessage = new ChatMessage(email, "left the chat!", Instant.now().getEpochSecond());
            broadcastMessage(chat, leaveMessage);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            ChatMessage incomingMessage = gson.fromJson(text, ChatMessage.class);

            Map<String, Channel> chat = CHATS.get(chatId);
            if (chat != null) {
                broadcastMessage(chat, incomingMessage);
            }
        } else if (frame instanceof CloseWebSocketFrame) {
            ctx.channel().close();
        }
    }

    private void broadcastMessage(Map<String, Channel> chat, ChatMessage chatMessage) {
        String jsonMessage = gson.toJson(chatMessage);
        chat.values().forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(jsonMessage)));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
