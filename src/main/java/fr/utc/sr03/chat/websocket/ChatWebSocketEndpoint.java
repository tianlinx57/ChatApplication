package fr.utc.sr03.chat.websocket;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{chatId}/{email}")
public class ChatWebSocketEndpoint {

    public static final Map<Long, Map<String, Session>> CHATS = new ConcurrentHashMap<>();

    private static final Gson gson = new Gson();

    @OnOpen
    public synchronized void onOpen(@PathParam("chatId") Long chatId, @PathParam("email") String email, Session session) {
        Map<String, Session> chat = CHATS.getOrDefault(chatId, new ConcurrentHashMap<>());
        chat.put(email, session);
        CHATS.put(chatId, chat);
        System.out.println("WebSocket connection established for chatId: " + chatId + " and email: " + email);

        // 通知聊天室的所有用户有新用户加入了
        ChatMessage chatMessage = new ChatMessage(email, "joined the chat!", Instant.now().getEpochSecond());
        broadcastMessage(chat, chatMessage);
    }

    @OnMessage
    public synchronized void onMessage(@PathParam("chatId") Long chatId, @PathParam("email") String email, String message) {
        Map<String, Session> chat = CHATS.get(chatId);

        // 解析JSON消息
        Gson gson = new Gson();
        ChatMessage incomingMessage = gson.fromJson(message, ChatMessage.class);

        // 从解析的JSON中获取email和content
        String incomingEmail = incomingMessage.getEmail();
        String incomingContent = incomingMessage.getContent();
        long incomingTimestamp = incomingMessage.getTimestamp();

        // 创建ChatMessage对象
        ChatMessage chatMessage = new ChatMessage(incomingEmail, incomingContent, incomingTimestamp);
        //System.out.println(chatMessage);

        // 将ChatMessage对象广播给聊天室的所有用户
        broadcastMessage(chat, chatMessage);
    }


    @OnClose
    public synchronized void onClose(@PathParam("chatId") Long chatId, @PathParam("email") String email) {
        Map<String, Session> chat = CHATS.get(chatId);

        System.out.println("Closed for chatId: " + chatId + " and email: " + email);

        if (chat != null) {
            chat.remove(email);

            // 通知聊天室的所有用户有用户离开了
            ChatMessage chatMessage = new ChatMessage(email, "left the chat!", Instant.now().getEpochSecond());
            broadcastMessage(chat, chatMessage);
        }

    }

    private void broadcastMessage(Map<String, Session> chat, ChatMessage chatMessage) {
        String jsonMessage = gson.toJson(chatMessage);
        chat.values().forEach(session -> {
            try {
                session.getBasicRemote().sendText(jsonMessage);
                //System.out.println("jsonMessage: " + jsonMessage);
            } catch (IOException e) {
                // 发送消息时发生异常，可以选择关闭 WebSocket 连接或采取其他适当的处理方式
                e.printStackTrace();
                System.err.println("Failed to send message: " + e.getMessage());
                closeSession(session);
            }
        });
    }

    private void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
