package fr.utc.sr03.chat.service;

import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    void removeUser(User user);
    void updateUser(User user);
    void createUser(User user);
    User getUser(String mail, String password);

    User getUserById(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> searchUsersByUsername(String username);
    List<User> searchDeactivatedUsersByUsername(String username);

    List<Chat> getProprietaireChats(long userId);

    List<Chat> getAllChats(long userId);

    List<Chat> getUserChats(long userId);
}
