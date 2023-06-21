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
import fr.utc.sr03.chat.config.SpringApplicationContextHolder;
import fr.utc.sr03.chat.dao.ChatRepository;
import org.springframework.context.ApplicationContext;

@Component
@ServerEndpoint("/websocket/{chatId}/{email}")
public class ChatWebSocketEndpoint {

    public static final Map<Long, Map<String, Session>> CHATS = new ConcurrentHashMap<>();

    private static final Gson gson = new Gson();

    private final ChatRepository chatRepository;

    public ChatWebSocketEndpoint() {
        ApplicationContext applicationContext = SpringApplicationContextHolder.getContext();
        this.chatRepository = applicationContext.getBean(ChatRepository.class);
    }

    private Instant getChatDeadline(Long chatId) {
        // 使用chatRepository来查询聊天的截止时间
        return chatRepository.findById(chatId)
                .map(chat -> chat.getDeadline().toInstant())
                .orElse(Instant.MAX);
    }

    @OnOpen
    public synchronized void onOpen(@PathParam("chatId") Long chatId, @PathParam("email") String email, Session session) {
        // 检查聊天是否存在
        if (!chatRepository.existsById(chatId)) {
            closeSession(session);
            return;
        }

        Map<String, Session> chat = CHATS.getOrDefault(chatId, new ConcurrentHashMap<>());
        chat.put(email, session);
        CHATS.put(chatId, chat);

        // 检查当前时间是否超过截止时间
        Instant deadline = getChatDeadline(chatId);
        Instant now = Instant.now();
        if (now.isAfter(deadline)) {
            chatRepository.deleteById(chatId);
            chat.values().forEach(this::closeSession);
            return; // 提前退出，不发送消息
        }

        for (String existingEmail : chat.keySet()) {
            if (!existingEmail.equals(email)) { // 不要给自己发送消息
                ChatMessage existingUserMessage = new ChatMessage(existingEmail, "joined the chat!", Instant.now().getEpochSecond());
                sendMessageToUser(session, existingUserMessage);
            }
        }


        // 通知聊天室的所有用户有新用户加入了
        ChatMessage chatMessage = new ChatMessage(email, "joined the chat!", Instant.now().getEpochSecond());
        broadcastMessage(chat, chatMessage);
    }

    private void sendMessageToUser(Session session, ChatMessage chatMessage) {
        String jsonMessage = gson.toJson(chatMessage);
        try {
            session.getBasicRemote().sendText(jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to send message: " + e.getMessage());
            closeSession(session);
        }
    }

    @OnMessage
    public synchronized void onMessage(@PathParam("chatId") Long chatId, @PathParam("email") String email, String message) {
        Map<String, Session> chat = CHATS.get(chatId);

        // 获取聊天的截止时间
        Instant deadline = getChatDeadline(chatId);
        Instant now = Instant.now();

        // 检查当前时间是否超过截止时间
        if (now.isAfter(deadline)) {
            // 关闭所有与该聊天关联的会话
            chat.values().forEach(this::closeSession);
            chatRepository.deleteById(chatId);
            return; // 提前退出，不发送消息
        }

        // 解析JSON消息
        ChatMessage incomingMessage = gson.fromJson(message, ChatMessage.class);

        // 从解析的JSON中获取email和content
        String incomingEmail = incomingMessage.getEmail();
        String incomingContent = incomingMessage.getContent();
        long incomingTimestamp = incomingMessage.getTimestamp();

        // 创建ChatMessage对象
        ChatMessage chatMessage = new ChatMessage(incomingEmail, incomingContent, incomingTimestamp);

        // 将ChatMessage对象广播给聊天室的所有用户
        broadcastMessage(chat, chatMessage);
    }


    @OnClose
    public synchronized void onClose(@PathParam("chatId") Long chatId, @PathParam("email") String email) {
        Map<String, Session> chat = CHATS.get(chatId);

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
            } catch (IOException e) {
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
