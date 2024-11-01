package fr.utc.sr03.chat;

import fr.utc.sr03.chat.dao.ChatRepository;
import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.User;
import fr.utc.sr03.chat.service.UserService;
import fr.utc.sr03.chat.websocket_netty.ChatWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ChatApplication.class, args);
    }


    @Autowired
    private UserService userService;
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // Create a new user
            User user = new User();
            user.setFirstName("Linxiao");
            user.setLastName("TIAN");
            user.setMail("1002179940@qq.com");
            user.setPassword("tlx200057");
            user.setAdmin(true);

            // Add the user to the database through the user service
            userService.createUser(user);
        };
    }

    @Bean
    public CommandLineRunner demoData(UserRepository userRepo, ChatRepository chatRepo) {
        return args -> {
            // Creating users only if they don't exist
            User user1 = createUserIfNotExists(userRepo, "Tian", "Linxiao", "linxiao.tian@etu.utc.fr", "tlx200057", true);
            User user2 = createUserIfNotExists(userRepo, "Du", "Xinyu", "xinyu.du@etu.utc.fr", "password", true);
            User user3 = createUserIfNotExists(userRepo, "wang", "hongzhe", "wang@etu.utc.fr", "password", false);

            // Create some chats
            Chat chat1 = new Chat();
            chat1.setNom("Chat 1");
            chat1.setCreateDate(new Date());
            chat1.setDeadline(getFutureDate(7)); // Deadline set to one week from now
            chat1.setDescription("Description for Chat 1");
            chat1.setProprietaire(user1);
            chat1.setUsers(Arrays.asList(user2, user3));

            Chat chat2 = new Chat();
            chat2.setNom("Chat 2");
            chat2.setCreateDate(new Date());
            chat2.setDeadline(getFutureDate(14)); // Deadline set to two weeks from now
            chat2.setDescription("Description for Chat 2");
            chat2.setProprietaire(user2);
            chat2.setUsers(Arrays.asList(user1, user3));

            // Save chats
            chatRepo.saveAll(Arrays.asList(chat1, chat2));
        };
    }

    private User createUserIfNotExists(UserRepository userRepo, String firstName, String lastName, String email, String password, boolean isAdmin) {
        Optional<User> existingUser = userRepo.findByMail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMail(email);
            user.setPassword(password);
            user.setAdmin(isAdmin);
//            userRepo.save(user);
            
            userService.createUser(user);
            return user;
        }
    }


    private Date getFutureDate(int daysInFuture) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysInFuture);
        return calendar.getTime();
    }
}
//package fr.utc.sr03.chat;
//
//import fr.utc.sr03.chat.model.User;
//import fr.utc.sr03.chat.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@SpringBootApplication
//public class ChatApplication {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public static void main(String[] args) {
//        SpringApplication.run(ChatApplication.class, args);
//    }
//
//    @Bean
//    public CommandLineRunner encryptExistingUserPasswords() {
//        return args -> {
//            // Fetch all users from the database
//            var users = userService.getAllUsers();
//
//            // Encrypt passwords and update them in the database
//            for (User user : users) {
//                // Assuming that you know that the passwords are not encrypted yet.
//                String encryptedPassword = passwordEncoder.encode(user.getPassword());
//                user.setPassword(encryptedPassword);
//
//                // Save the updated user back to the database
//                userService.updateUser(user);
//            }
//        };
//    }
//}
