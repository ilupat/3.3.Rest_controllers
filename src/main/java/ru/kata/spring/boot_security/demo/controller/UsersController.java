package ru.kata.spring.boot_security.demo.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UsersController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("login")
    public String log() {
        return "login";
    }

    @GetMapping("admin")
    public String getAllUsers(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("listRoles", roleService.getAllRoles());
        model.addAttribute("listUser", userService.getAllUsers());
        model.addAttribute("user", user);
        return "adminPage";
    }

    @GetMapping("user")
    public String infoUser(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        return "userPage";
    }

    @GetMapping(value = "admin/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "redirect:/admin";
    }

    @PostMapping(value = "admin/new")
    public String newUser(@ModelAttribute User user,
                          @RequestParam(value = "roless") String[] role) throws NotFoundException {
        Set<Role> rolesSet = new HashSet<>();
        for (String roles : role) {
            rolesSet.add(roleService.getByName(roles));
        }
        user.setRoles(rolesSet);
        userService.save(user);
        return "redirect:/admin";
    }
    @PostMapping(value = "admin/{id}")
    public String editUser(@ModelAttribute User user,
                           @RequestParam(value = "roless") String[] role) throws NotFoundException {
        Set<Role> rolesSet = new HashSet<>();
        for (String roles : role) {
            rolesSet.add((roleService.getByName(roles)));
        }
        user.setRoles(rolesSet);
        userService.edit(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "admin/{id}/del")
    public String deleteUser(@PathVariable("id") long id) {
        User user = userService.getById(id);
        userService.delete(user);
        return "redirect:/admin";
    }
}
