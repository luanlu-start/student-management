/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.User;
import vn.edu.fpt.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Legion
 */
@Controller
public class loginController {

    private final UserService userService;

    @Autowired
    public loginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
            @ModelAttribute("loginForm") LoginForm form,
            HttpServletRequest request,
            Model model) {

        if (form.getAction() != null && !"login".equals(form.getAction())) {
            model.addAttribute("error", "Unsupported action");
            return "login";
        }

        User user = userService.checkLogin(form.getUsername(), form.getPassword());
        if (user == null) {
            model.addAttribute("error", "Incorrect username or password!");
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return "redirect:/home";
    }

    public static class LoginForm {
        private String action;
        private String username;
        private String password;

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

}

