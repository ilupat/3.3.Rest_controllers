package ru.kata.spring.boot_security.demo.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/")
public class UsersRestController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;


    @Autowired
    public UsersRestController(UserService userService, RoleService roleService, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @GetMapping("login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }
    @GetMapping("admin")
    public ModelAndView index(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", userRepository.findByUsername(principal.getName()));
        return modelAndView;
    }

    @GetMapping("userPage")
    public ModelAndView forUser(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("userPage");
        User user = userRepository.findByUsername(principal.getName());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("getUser")
    public ResponseEntity<User> getUserC(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("admin/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("admin")
    public ResponseEntity<User> newUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO);
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDTO.getRoleNames()) {
            roles.add(roleService.getByName(roleName));
        }
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("admin/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("admin/{id}")
    public ResponseEntity<User> getUserId(@PathVariable(name = "id") long id) {
        User user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @SneakyThrows
    @PutMapping("admin")
    public ResponseEntity<User> editUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO);
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDTO.getRoleNames()) {
            roles.add(roleService.getByName(roleName));
        }
        user.setRoles(roles);
        userService.edit(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("admin/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        User user = userService.getById(id);
        userService.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
