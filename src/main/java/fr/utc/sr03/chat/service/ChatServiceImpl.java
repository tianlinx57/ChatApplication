package fr.utc.sr03.chat.service;

import fr.utc.sr03.chat.dao.ChatRepository;
import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.ChatDTO;
import fr.utc.sr03.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService{
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService;
    @Override
    public Optional<Chat> getChat(long chatId){
        return chatRepository.findById(chatId);
    }

    @Override
    public void deleteChat(long chatId) {
        chatRepository.deleteById(chatId);
    }


    public Chat createChat(ChatDTO chatData) {
        // Create new Chat object
        Chat chat = new Chat();
        chat.setNom(chatData.getTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date deadline = dateFormat.parse(chatData.getDeadline());
            chat.setDeadline(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parse exception
        }
        chat.setDescription(chatData.getDescription());
        chat.setCreateDate(new Date());
        // Set owner (proprietaire)
        Optional<User> ownerOptional = userService.findUserByEmail(chatData.getOwnerEmail());
        ownerOptional.ifPresent(chat::setProprietaire);

        // Add members
        List<User> chatMembers = new ArrayList<>();
        for (String memberEmail : chatData.getMembers()) {
            Optional<User> userOptional = userService.findUserByEmail(memberEmail);
            userOptional.ifPresent(chatMembers::add);
        }

        // Set members to chat
        chat.setUsers(chatMembers);

        // Save chat object in database
        return chatRepository.save(chat);
    }




    public Chat updateChat(long id, ChatDTO chatData) {
        // Recherche du chat dans la base de données
        Optional<Chat> optionalChat = chatRepository.findById(id);

        // Si le chat est trouvé
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();

            // Mise à jour des attributs du chat avec les données provenant du DTO
            chat.setNom(chatData.getTitle());
            chat.setDescription(chatData.getDescription());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            try {
                Date deadline = dateFormat.parse(chatData.getDeadline());
                chat.setDeadline(deadline);
            } catch (ParseException e) {
                e.printStackTrace();
                // Gérer l'exception de parsing
            }

            List<User> chatMembers = new ArrayList<>();
            for (String memberEmail : chatData.getMembers()) {
                Optional<User> userOptional = userService.findUserByEmail(memberEmail);
                userOptional.ifPresent(chatMembers::add);
            }
            chat.setUsers(chatMembers);

            // Enregistrer la mise à jour du chat dans la base de données
            return chatRepository.save(chat);
        } else {
            return null;
        }
    }

}
