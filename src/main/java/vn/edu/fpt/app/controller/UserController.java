/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.User;
import vn.edu.fpt.app.service.LecturerService;
import vn.edu.fpt.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Legion
 */
@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('admin', 'academic_staff')")
public class UserController {

    private final UserService userService;
    private final LecturerService lecturerService;

    public UserController(UserService userService, LecturerService lecturerService) {
        this.userService = userService;
        this.lecturerService = lecturerService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("listUser", list);
        model.addAttribute("home_view", "user/user.html");
        return "dashboard";
    }

    @GetMapping("/add")
    public String showAdd(HttpSession session, Model model) {
        List<Lecturer> lecturerList = lecturerService.getAllLecturers();
        model.addAttribute("listLecturers", lecturerList);
        model.addAttribute("home_view", "user/createUser.html");
        return "dashboard";
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/delete")
    public String showDelete(
            @RequestParam(name = "id") Integer id,
            HttpSession session,
            Model model) {

        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            session.setAttribute("message", "You do not have permission to access that page.");
            session.setAttribute("messageType", "error");
            return "redirect:/user";
        }

        try {
            if (loginUser.getId() == id) {
                session.setAttribute("message", "You cannot delete your own account!");
                session.setAttribute("messageType", "error");
                return "redirect:/user";
            }

            User userToDelete = userService.getUserById(id);
            if (userToDelete != null) {
                model.addAttribute("userToDelete", userToDelete);
                model.addAttribute("home_view", "user/deleteUser.html");
                return "dashboard";
            }
            return "redirect:/user";
        } catch (Exception e) {
            return "redirect:/user";
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userForm") UserForm form, HttpSession session) {

        try {
            Lecturer lecturer = null;
            String role = form.getRole() == null ? "" : form.getRole().trim().toLowerCase().replace(' ', '_');
            if (("teacher".equals(role) || "lecturer".equals(role))
                    && form.getLecturerId() != null && form.getLecturerId() > 0) {
                lecturer = lecturerService.getLecturerById(form.getLecturerId());
            }

            User newUser = new User(form.getUsername(), form.getPassword(), role, lecturer);
            boolean success = userService.addNewUser(newUser);

            if (success) {
                session.setAttribute("message", "User '" + form.getUsername() + "' created successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to create user. Username might already exist.");
                session.setAttribute("messageType", "error");
            }
        } catch (Exception e) {
            session.setAttribute("message", "Error creating user: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        return "redirect:/user";
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/delete")
    public String delete(@ModelAttribute("userForm") UserForm form, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            session.setAttribute("message", "You do not have permission to perform this action.");
            session.setAttribute("messageType", "error");
            return "redirect:/user";
        }

        try {
            if (form.getUserId() == null) {
                return "redirect:/user";
            }

            if (loginUser.getId() == form.getUserId()) {
                session.setAttribute("message", "You cannot delete your own account!");
                session.setAttribute("messageType", "error");
            } else {
                boolean success = userService.deleteUserById(form.getUserId());

                if (success) {
                    session.setAttribute("message", "User deleted successfully!");
                    session.setAttribute("messageType", "success");
                } else {
                    session.setAttribute("message", "Failed to delete user.");
                    session.setAttribute("messageType", "error");
                }
            }
        } catch (Exception e) {
            session.setAttribute("message", "Error deleting user: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        return "redirect:/user";
    }

    public static class UserForm {
        private Integer userId;
        private String username;
        private String password;
        private String role;
        private Integer lecturerId;

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Integer getLecturerId() { return lecturerId; }
        public void setLecturerId(Integer lecturerId) { this.lecturerId = lecturerId; }
    }
}
