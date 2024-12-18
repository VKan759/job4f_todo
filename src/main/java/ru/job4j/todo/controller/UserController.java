package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@ThreadSafe

@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        model.addAttribute("zones", zones);
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user, @RequestParam(required = false) String zone) {
        if (zone == null || zone.isEmpty()) {
            zone = "UTC";
        }
        user.setTimezone(zone);
        Optional<User> save = userService.save(user);
        if (save.isEmpty()) {
            model.addAttribute("message", "пользователь существует");
            return "errors/404";
        }
        return "redirect:/users/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest httpServletRequest, @ModelAttribute User user) {
        Optional<User> byEmailAndPassword = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (byEmailAndPassword.isEmpty()) {
            model.addAttribute("message", "Пользователь не найден");
            return "errors/404";
        }
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("user", byEmailAndPassword.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
