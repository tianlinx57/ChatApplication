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

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("accueil")
    public String getAdminHome(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/accueil";
    }

    @GetMapping("users")
    public String getUserList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/accueil";
    }

    @GetMapping("nouveau_utilisateur")
    public String nouveau_utilisateur(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/admin/nouveau_utilisateur";
    }

    @GetMapping("utilisateurs_desactives")
    public String utilisateurs_desactives(Model model) {
        return "/admin/utilisateurs_desactives";
    }

    @GetMapping("users/add")
    public String getUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/nouveau_utilisateur";
    }

    @PostMapping("users/add")
    public String addUser(@ModelAttribute User user, Model model) {
        System.out.println("===> LastName = " + user.getLastName());
        System.out.println("===> FirstName = " + user.getFirstName());
        user.setMail(user.getMail());
        user.setPassword(user.getPassword());
        user.setAdmin(false);

        userService.updateUser(user);
        return "admin/accueil";
    }
}
