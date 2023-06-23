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
        // Utilise chatRepository pour obtenir la date limite du chat
        return chatRepository.findById(chatId)
                .map(chat -> chat.getDeadline().toInstant())
                .orElse(Instant.MAX);
    }

    @OnOpen
    public synchronized void onOpen(@PathParam("chatId") Long chatId, @PathParam("email") String email, Session session) {
        // Vérifie si le chat existe
        if (!chatRepository.existsById(chatId)) {
            closeSession(session);
            return;
        }

        Map<String, Session> chat = CHATS.getOrDefault(chatId, new ConcurrentHashMap<>());
        chat.put(email, session);
        CHATS.put(chatId, chat);

        // Vérifie si l'heure actuelle dépasse la date limite
        Instant deadline = getChatDeadline(chatId);
        Instant now = Instant.now();
        if (now.isAfter(deadline)) {
            chatRepository.deleteById(chatId);
            chat.values().forEach(this::closeSession);
            return; // Quitte prématurément, ne pas envoyer de message
        }

        for (String existingEmail : chat.keySet()) {
            if (!existingEmail.equals(email)) { // Ne pas envoyer de message à soi-même
                ChatMessage existingUserMessage = new ChatMessage(existingEmail, "joined the chat!", Instant.now().getEpochSecond());
                sendMessageToUser(session, existingUserMessage);
            }
        }

        // Notifie tous les utilisateurs du salon de discussion qu'un nouvel utilisateur a rejoint
        ChatMessage chatMessage = new ChatMessage(email, "joined the chat!", Instant.now().getEpochSecond());
        broadcastMessage(chat, chatMessage);
    }

    private void sendMessageToUser(Session session, ChatMessage chatMessage) {
        String jsonMessage = gson.toJson(chatMessage);
        try {
            session.getBasicRemote().sendText(jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossible d'envoyer le message : " + e.getMessage());
            closeSession(session);
        }
    }

    @OnMessage
    public synchronized void onMessage(@PathParam("chatId") Long chatId, @PathParam("email") String email, String message) {
        Map<String, Session> chat = CHATS.get(chatId);

        // Obtient la date limite du chat
        Instant deadline = getChatDeadline(chatId);
        Instant now = Instant.now();

        // Vérifie si l'heure actuelle dépasse la date limite
        if (now.isAfter(deadline)) {
            // Ferme toutes les sessions associées à ce chat
            chat.values().forEach(this::closeSession);
            chatRepository.deleteById(chatId);
            return; // Quitte prématurément, ne pas envoyer de message
        }

        // Analyse le message JSON
        ChatMessage incomingMessage = gson.fromJson(message, ChatMessage.class);

        // Récupère l'e-mail et le contenu à partir du JSON analysé
        String incomingEmail = incomingMessage.getEmail();
        String incomingContent = incomingMessage.getContent();
        long incomingTimestamp = incomingMessage.getTimestamp();

        // Crée un objet ChatMessage
        ChatMessage chatMessage = new ChatMessage(incomingEmail, incomingContent, incomingTimestamp);

        // Diffuse le message ChatMessage à tous les utilisateurs du salon de discussion
        broadcastMessage(chat, chatMessage);
    }


    @OnClose
    public synchronized void onClose(@PathParam("chatId") Long chatId, @PathParam("email") String email) {
        Map<String, Session> chat = CHATS.get(chatId);

        if (chat != null) {
            chat.remove(email);
            // Notifie tous les utilisateurs du salon de discussion qu'un utilisateur a quitté
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
                System.err.println("Impossible d'envoyer le message : " + e.getMessage());
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
