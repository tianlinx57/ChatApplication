package fr.utc.sr03.chat.service;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void removeUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(String mail, String password) {
        Optional<User> userOptional = userRepository.findByMail(mail);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public List<Chat> getProprietaireChats(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getChats_proprietaire();
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }

    @Override
    public List<Chat> getAllChats(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            List<Chat> allChats = new ArrayList<>();
            allChats.addAll(user.get().getChats_user());
            allChats.addAll(user.get().getChats_proprietaire());
            return allChats;
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }

    @Override
    public List<Chat> getUserChats(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getChats_user();
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }
}
