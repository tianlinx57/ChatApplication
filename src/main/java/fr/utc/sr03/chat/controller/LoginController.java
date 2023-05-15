package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = {"/", "/login"})
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping
    public String postLogin(@ModelAttribute User user, Model model) {
        System.out.println("===> mail = " + user.getMail());
        System.out.println("===> password = " + user.getPassword());

        // Fetch the user from the repository by email
        Optional<User> existingUser = userRepository.findByMail(user.getMail());

        // Check if the user exists
        if(existingUser.isEmpty()) {
            model.addAttribute("error", "No user found with the provided email.");
            return "login";
        }

        // Check if the password is correct
        if(!existingUser.get().getPassword().equals(user.getPassword())) {
            model.addAttribute("error", "Incorrect password.");
            return "login";
        }

        return "redirect:/admin/accueil";
    }


}
