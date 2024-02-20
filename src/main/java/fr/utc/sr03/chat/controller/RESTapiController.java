package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.ChatDTO;
import fr.utc.sr03.chat.model.User;
import fr.utc.sr03.chat.service.ChatService;
import fr.utc.sr03.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RESTapiController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    // Récupérer tous les chats d'un propriétaire
    @GetMapping("/proprietaire_chats/{id}")
    public List<Chat> getProprietaireChats(@PathVariable("id") long userId) {
        return userService.getProprietaireChats(userId);
    }

    // Récupérer tous les utilisateurs
    @GetMapping("/all_users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Récupérer un chat par son identifiant
    @GetMapping("/chat/{id}")
    public Optional<Chat> getChats(@PathVariable("id") long chatId) {
        return chatService.getChat(chatId);
    }

    // Récupérer tous les chats d'un utilisateur
    @GetMapping("/all_chats/{id}")
    public List<Chat> getAllChats(@PathVariable("id") long userId) {
        return userService.getAllChats(userId);
    }

    // Récupérer les chats d'un utilisateur
    @GetMapping("/user_chats/{id}")
    public List<Chat> getUserChats(@PathVariable("id") long userId) {
        return userService.getUserChats(userId);
    }

    // Connexion de l'utilisateur
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("===> mail = " + loginRequest.getMail());
        System.out.println("===> password = " + loginRequest.getPassword());
        User existingUser = userService.getUser(loginRequest.getMail(), loginRequest.getPassword());
        if (existingUser != null) {
            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Adresse e-mail ou mot de passe incorrect");
        }
    }

    // Créer un nouveau chat
    @PostMapping("/chat")
    public ResponseEntity<Chat> createChat(@RequestBody ChatDTO chatData) {
        try {
            Chat newChat = chatService.createChat(chatData);
            return new ResponseEntity<>(newChat, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour un chat existant
    @PutMapping("/chat/{id}")
    public ResponseEntity<Void> updateChat(@PathVariable Long id, @RequestBody ChatDTO chatData) {
        try {
            chatService.updateChat(id, chatData);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer un chat
    @DeleteMapping("/chat/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        try {
            chatService.deleteChat(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Classe interne pour la requête de connexion
    public static class LoginRequest {
        private String mail;
        private String password;
        public String getMail() {
            return mail;
        }
        public void setMail(String mail) {
            this.mail = mail;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

}
