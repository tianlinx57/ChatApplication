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
        return "redirect:/admin/accueil";
    }

    @GetMapping("edit")
    public String editUser(@RequestParam("id") Long userId, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(userId);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "User didn't exist!");
            return "redirect:/admin/accueil";
        } else {
            model.addAttribute("user", user);
        }
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "User updated successfully");
            return "redirect:/admin/accueil";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating the user");
            return "redirect:/admin/accueil";
        }
    }


    @GetMapping("/supp")
    public String deleteUser(@RequestParam("id") Long userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            userService.removeUser(user);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while deleting the user");
        }
        return "redirect:/admin/accueil";
    }


}
