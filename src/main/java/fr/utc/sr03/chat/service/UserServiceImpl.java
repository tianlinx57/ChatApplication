package fr.utc.sr03.chat.service;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        // 在保存之前加密密码
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    public void createUser(User user) {
        // 在保存之前加密密码
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    @Override
    public User getUser(String mail, String password) {
        Optional<User> userOptional = userRepository.findByMail(mail);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(passwordEncoder.matches(password, user.getPassword())){
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

    public List<User> searchUsersByUsername(String username) {
        List<User> allUsers = userRepository.findAll(); // 假设所有用户存在于 UserRepository

        List<User> searchResults = allUsers.stream()
                .filter(user -> user.getFirstName().contains(username) || user.getLastName().contains(username))
                .collect(Collectors.toList());

        return searchResults;
    }
    public List<User> searchDeactivatedUsersByUsername(String username) {
        List<User> allUsers = userRepository.findAll(); // Assuming all users are stored in UserRepository

        List<User> searchResults = allUsers.stream()
                .filter(user -> user.isDisabled() && (user.getFirstName().contains(username) || user.getLastName().contains(username)))
                .collect(Collectors.toList());

        return searchResults;
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

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByMail(email);
    }
}
