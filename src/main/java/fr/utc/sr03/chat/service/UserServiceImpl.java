package fr.utc.sr03.chat.service;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;



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

    public List<User> searchUsersByUsername(String username) {
        List<User> allUsers = userRepository.findAll(); // 假设所有用户存在于 UserRepository

        List<User> searchResults = allUsers.stream()
                .filter(user -> user.getFirstName().contains(username) || user.getLastName().contains(username))
                .collect(Collectors.toList());

        return searchResults;
    }

    public int getTotalUsers() {
        return (int) userRepository.count();
    }
    public List<User> getUsersByPage(int page, int pageSize) {
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.getContent();
    }
}
