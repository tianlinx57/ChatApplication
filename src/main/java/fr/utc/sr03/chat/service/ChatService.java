package fr.utc.sr03.chat.service;


import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.ChatDTO;

import java.util.Optional;

public interface ChatService {
    Optional<Chat> getChat(long chatId);

    void deleteChat(long chatId);

    Chat createChat(ChatDTO chatData);
    Chat updateChat(long id,ChatDTO chatData);
}
