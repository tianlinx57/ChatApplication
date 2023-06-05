package fr.utc.sr03.chat;

import fr.utc.sr03.chat.dao.ChatRepository;
import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demoData(UserRepository userRepo, ChatRepository chatRepo) {
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
//            user2.setAdmin(true);
//
//            User user3 = new User();
//            user3.setFirstName("wang");
//            user3.setLastName("hongzhe");
//            user3.setMail("wang@etu.utc.fr");
//            user3.setPassword("password");
//            user3.setAdmin(false);
//
//            // 创建一些聊天
//            Chat chat1 = new Chat();
//            chat1.setNom("Chat 1");
//            chat1.setCreateDate(new Date());
//            chat1.setDescription("Description for Chat 1");
//            chat1.setProprietaire(user1);
//
//            Chat chat2 = new Chat();
//            chat2.setNom("Chat 2");
//            chat2.setCreateDate(new Date());
//            chat2.setDescription("Description for Chat 2");
//            chat2.setProprietaire(user2);
//            chat2.setUsers(Arrays.asList(user3));
//
//            // 设置用户的聊天
//            user1.setChats_proprietaire(Arrays.asList(chat1));
//            user2.setChats_proprietaire(Arrays.asList(chat2));
//            user3.setChats_user(Arrays.asList(chat2));
//
//            // 保存用户和聊天
//            userRepo.saveAll(Arrays.asList(user1, user2, user3));
//            chatRepo.saveAll(Arrays.asList(chat1, chat2));
//        };
//    }
}
