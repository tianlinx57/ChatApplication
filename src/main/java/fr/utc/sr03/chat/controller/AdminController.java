package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.User;
import fr.utc.sr03.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private UserService userService;

    // Page d'accueil de l'administrateur
    @GetMapping("accueil")
    public String getAdminHome(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/accueil";
    }

    // Liste des utilisateurs
    @GetMapping("users")
    public String getUserList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/accueil";
    }

    // Page de création d'un nouvel utilisateur
    @GetMapping("nouveau_utilisateur")
    public String nouveau_utilisateur(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/admin/nouveau_utilisateur";
    }

    // Page des utilisateurs désactivés
    @GetMapping("utilisateurs_desactives")
    public String utilisateurs_desactives(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/admin/utilisateurs_desactives";
    }

    // Formulaire de création d'un utilisateur
    @GetMapping("users/add")
    public String getUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/nouveau_utilisateur";
    }

    // Ajouter un utilisateur
    @PostMapping("users/add")
    public String addUser(@ModelAttribute User user, Model model) {
        System.out.println("===> LastName = " + user.getLastName());
        System.out.println("===> FirstName = " + user.getFirstName());
        user.setMail(user.getMail());
        user.setPassword(user.getPassword());
        user.setAdmin(false);

        userService.createUser(user);
        return "redirect:/admin/accueil";
    }

    // Modifier un utilisateur
    @GetMapping("edit")
    public String editUser(@RequestParam("id") Long userId, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(userId);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "L'utilisateur n'existe pas !");
            return "redirect:/admin/accueil";
        } else {
            model.addAttribute("user", user);
        }
        return "admin/edit";
    }

    // Mettre à jour un utilisateur
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "L'utilisateur a été mis à jour avec succès");
            return "redirect:/admin/accueil";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur s'est produite lors de la mise à jour de l'utilisateur");
            return "redirect:/admin/accueil";
        }
    }

    // Supprimer un utilisateur
    @GetMapping("/supp")
    public String deleteUser(@RequestParam("id") Long userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            userService.removeUser(user);
            redirectAttributes.addFlashAttribute("message", "L'utilisateur a été supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur s'est produite lors de la suppression de l'utilisateur");
        }
        return "redirect:/admin/accueil";
    }

    // Désactiver un utilisateur
    @GetMapping("/disable")
    public String disableUser(@RequestParam("id") Long userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            user.setDisabled(true);
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "L'utilisateur a été désactivé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur s'est produite lors de la désactivation de l'utilisateur");
        }
        return "redirect:/admin/utilisateurs_desactives";
    }

    // Activer un utilisateur
    @GetMapping("/activer")
    public String activereUser(@RequestParam("id") Long userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            user.setDisabled(false);
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "L'utilisateur a été activé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur s'est produite lors de l'activation de l'utilisateur");
        }
        return "redirect:/admin/accueil";
    }

    // Rechercher des utilisateurs
    @GetMapping("/recherche")
    public String searchUsers(@RequestParam("username") String username, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<User> searchResults = userService.searchUsersByUsername(username);
            model.addAttribute("users", searchResults);
            model.addAttribute("message", "Résultats de recherche pour : " + username);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur s'est produite lors de la recherche d'utilisateurs");
        }
        return "admin/accueil";
    }

    // Rechercher des utilisateurs désactivés
    @GetMapping("/recherche-deactives")
    public String searchDeactivatedUsers(@RequestParam("username") String username, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<User> searchResults = userService.searchDeactivatedUsersByUsername(username);
            model.addAttribute("users", searchResults);
            model.addAttribute("message", "Résultats de recherche pour : " + username);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur s'est produite lors de la recherche d'utilisateurs");
        }
        return "admin/utilisateurs_desactives";
    }
}
