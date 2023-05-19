package fr.utc.sr03.chat.service;

import fr.utc.sr03.chat.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void removeUser(User user);
    void updateUser(User user);
    User getUser(String mail, String password);
}
