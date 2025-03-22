package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.security.RoleService;
import ru.kata.spring.boot_security.demo.security.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String adminPage(@AuthenticationPrincipal User user, Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("user", user); // Передаем текущего пользователя в модель
        model.addAttribute("allRoles", roleService.findAll()); // Передаем список всех ролей
        return "admin";
    }

    // 2. Отображение формы для добавления нового пользователя (Create)
    @GetMapping("/new")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User()); // Передаем новый объект User
        model.addAttribute("roles", roleService.findAll()); // Передаем список ролей
        return "add-user";
    }

    // 3. Обработка данных из формы и сохранение пользователя (Create)
    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        userService.save(user); // Сохраняем пользователя
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно добавлен");
        return "redirect:/admin"; // Перенаправляем на страницу админа
    }

    // 4. Отображение формы для редактирования пользователя (Update)
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id); // Находим пользователя по ID
        List<Role> allRoles = roleService.findAll(); // Получаем список всех ролей
        model.addAttribute("user", user); // Передаем пользователя в модель
        model.addAttribute("allRoles", allRoles); // Передаем список ролей в модель
        return "edit-user"; // Возвращаем шаблон для редактирования пользователя
    }

    // 5. Обработка данных из формы и обновление пользователя (Update)
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        user.setId(id); // Убедимся, что ID пользователя не меняется
        userService.update(user);
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно обновлен");
        return "redirect:/admin";
    }

    // 6. Удаление пользователя (Delete)
    @GetMapping("/delete/{id}")
    public String showDeleteUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id); // Находим пользователя по ID
        model.addAttribute("user", user); // Передаем пользователя в модель
        return "delete-user"; // Возвращаем шаблон для подтверждения удаления
    }

    // 7. Обработка POST-запроса для удаления пользователя
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id); // Удаляем пользователя
        redirectAttributes.addFlashAttribute("message", "User successfully deleted");
        return "redirect:/admin"; // Перенаправляем на страницу админа
    }
}