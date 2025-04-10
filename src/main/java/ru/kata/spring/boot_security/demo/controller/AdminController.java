package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView adminPage(@AuthenticationPrincipal User currentUser) {
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("users", userService.findAll());
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("allRoles", roleService.findAll());
        modelAndView.addObject("newUser", new User());
        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView addUser(@Valid @ModelAttribute("newUser") User user,
                                BindingResult bindingResult,
                                @RequestParam("roles") List<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("admin");
            modelAndView.addObject("allRoles", roleService.findAll());
            return modelAndView;
        }

        try {
            userService.setUserRoles(user, roleIds);
            userService.save(user);
        } catch (Exception e) {
            // Обработка ошибок
        }

        return new ModelAndView("redirect:/admin");
    }


    @GetMapping("/edit")
    public ModelAndView editUserForm(@RequestParam("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("edit-user");
        modelAndView.addObject("user", userService.findById(id));
        modelAndView.addObject("allRoles", roleService.findAll());
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView updateUser(@RequestParam Long id,
                                   @ModelAttribute("user") User user,
                                   @RequestParam("roles") List<Long> roleIds) {
        userService.setUserRoles(user, roleIds);
        user.setId(id);
        userService.update(user);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/delete")
    public ModelAndView deleteUserForm(@RequestParam("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("delete-user");
        modelAndView.addObject("user", userService.findById(id));
        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return new ModelAndView("redirect:/admin");
    }
}