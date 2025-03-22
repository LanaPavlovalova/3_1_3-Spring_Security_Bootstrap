package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // Обработка главной страницы
    @GetMapping("/")
    public String home() {
        return "index";  // Возвращает шаблон index.html
    }

    // Обработка страницы входа (если не используется MvcConfig)
    @GetMapping("/login")
    public String login() {
        return "login";  // Возвращает шаблон login.html
    }
}
