package ru.kata.spring.boot_security.demo.service;


import javassist.NotFoundException;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void save(User user);

    void delete(User user);

    void edit(User user);

    User getById(long id);

    User getByName(String name) throws NotFoundException;
    User getByEmail(String email) throws NotFoundException;

}
