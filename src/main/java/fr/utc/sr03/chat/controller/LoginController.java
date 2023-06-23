package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.User;
import fr.utc.sr03.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = {"/", "/login"})
public class LoginController {
    @Autowired
    private UserService userService;

    // Afficher la page de connexion
    @GetMapping
    public String getLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Traitement de la connexion
    @PostMapping
    public String postLogin(@ModelAttribute User user, Model model, HttpServletRequest request) {
        System.out.println("===> mail = " + user.getMail());
        System.out.println("===> password = " + user.getPassword());

        // Récupérer l'utilisateur existant
        User existingUser = userService.getUser(user.getMail(), user.getPassword());
        System.out.println("===> admin = " + user.isAdmin());
        if (existingUser == null) {
            model.addAttribute("error", "Aucun utilisateur trouvé avec l'adresse e-mail fournie ou le mot de passe est incorrect.");
            return "login";
        }
        if (!existingUser.isAdmin()) {
            model.addAttribute("error", "Vous n'avez pas l'autorisation d'accéder à la page d'administration.");
            return "login";
        }
        request.getSession().setAttribute("email", user.getMail());
        return "redirect:/admin/accueil";
    }

}
