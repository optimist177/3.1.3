package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {


    private final UserService userService;

    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUserLsit(Model model) {
        List<User>  userList = userService.allUsers();
        model.addAttribute("userList",userList);
        return "/list";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model ) {
        model.addAttribute("user",userService.findUserBYId(id));
        return "/edit";
    }


    @PatchMapping("/update/{id}")
    public String update(@ModelAttribute User user, @PathVariable("id") Integer id) {
        userService.updateUser(id,user);
        return "redirect:/users";
    }


    @GetMapping("/user")
    public String index(Principal principal, Model model){
        User user =  userService.findByUsername(principal.getName());
        model.addAttribute("user",user);
        return "/userInfo";
    }


    @GetMapping("/create")
    public String register(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles",userService.listRoles());
        return "/create";
    }

    @PostMapping("/create")
    public String performRegister(@ModelAttribute("user") User user) {
        if (userService.saveUser(user)) {
            return "/user";
        }
        return "/user";
    }
}