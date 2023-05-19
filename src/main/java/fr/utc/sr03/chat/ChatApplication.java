package fr.utc.sr03.chat;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demoData(UserRepository repo) {
//        return args -> {
//            // 创建一些用户
//            User user1 = new User();
//            user1.setFirstName("Tian");
//            user1.setLastName("Linxiao");
//            user1.setMail("linxiao.tian@etu.utc.fr");
//            user1.setPassword("tlx200057");
//            user1.setAdmin(true);
//
//            User user2 = new User();
//            user2.setFirstName("Du");
//            user2.setLastName("Xinyu");
//            user2.setMail("xinyu.du@etu.utc.fr");
//            user2.setPassword("password");
//            user2.setAdmin(false);
//
//            // 保存用户
//            repo.saveAll(Arrays.asList(user1, user2));
//        };
//    }
}
