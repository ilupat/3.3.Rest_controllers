package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.security.Principal;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String log() {
        return "login";
    }

    @GetMapping("/admin")
    public String index(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("user", userRepository.findByUsername(principal.getName()));
        return "index";
    }

    @GetMapping("/userPage")
    public String forUser(ModelMap modelMap, Principal principal) {
        User us = userRepository.findByUsername(principal.getName());
        modelMap.addAttribute("user", us);
        return "userPage";
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUserC(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
