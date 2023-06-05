package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.User;
import fr.utc.sr03.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RESTapiController {

    @Autowired
    private UserService userService;

    @GetMapping("/proprietaire_chats/{id}")
    public List<Chat> getProprietaireChats(@PathVariable("id") long userId) {
        return userService.getProprietaireChats(userId);
    }

    @GetMapping("/all_chats/{id}")
    public List<Chat> getAllChats(@PathVariable("id") long userId) {
        return userService.getAllChats(userId);
    }

    @GetMapping("/user_chats/{id}")
    public List<Chat> getUserChats(@PathVariable("id") long userId) {
        return userService.getUserChats(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("===> mail = " + loginRequest.getMail());
        System.out.println("===> password = " + loginRequest.getPassword());
        User existingUser = userService.getUser(loginRequest.getMail(), loginRequest.getPassword());
        if (existingUser != null) {
            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }


    public static class LoginRequest {
        private String mail;
        private String password;
        public String getMail() {
            return mail;
        }
        public String setMail(String mail) {
            return this.mail=mail;
        }
        public String getPassword() {
            return password;
        }
        public String setPassword(String password) {
            return this.password=password;
        }
    }

}
