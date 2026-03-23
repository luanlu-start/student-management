/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.dto.UserCreateDTO;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.User;
import vn.edu.fpt.app.service.LecturerService;
import vn.edu.fpt.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String list(Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("listUser", list);
        model.addAttribute("home_view", "user/user.html");
        return "dashboard";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        List<Lecturer> lecturerList = lecturerService.getAllLecturers();
        model.addAttribute("listLecturers", lecturerList);
        if (!model.containsAttribute("userForm")) {
            UserCreateDTO form = new UserCreateDTO();
            form.setRole("lecturer");
            form.setLecturerId(0);
            model.addAttribute("userForm", form);
        }
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
    public String add(@Valid @ModelAttribute("userForm") UserCreateDTO form,
                      BindingResult result,
                      Model model,
                      HttpSession session) {

        String normalizedRole = form.getRole() == null ? "" : form.getRole().trim().toLowerCase().replace(' ', '_');
        if (!("admin".equals(normalizedRole) || "academic_staff".equals(normalizedRole) || "lecturer".equals(normalizedRole))) {
            result.rejectValue("role", "error.role", "Invalid role");
        }

        if (form.getPassword() != null && form.getConfirmPassword() != null
                && !form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Confirm password does not match");
        }

        if (form.getUsername() != null && userService.existsByUsername(form.getUsername())) {
            result.rejectValue("username", "error.username", "Username already exists");
        }

        if ("lecturer".equals(normalizedRole) && (form.getLecturerId() == null || form.getLecturerId() <= 0)) {
            result.rejectValue("lecturerId", "error.lecturerId", "Please select lecturer for lecturer role");
        }

        if ("lecturer".equals(normalizedRole)
                && form.getLecturerId() != null
                && form.getLecturerId() > 0
                && userService.existsByLecturerId(form.getLecturerId())) {
            result.rejectValue("lecturerId", "error.lecturerId", "This lecturer already has an account");
        }

        if (result.hasErrors()) {
            model.addAttribute("listLecturers", lecturerService.getAllLecturers());
            model.addAttribute("home_view", "user/createUser.html");
            return "dashboard";
        }

        try {
            Lecturer lecturer = null;
            if ("lecturer".equals(normalizedRole)) {
                lecturer = lecturerService.getLecturerById(form.getLecturerId());
                if (lecturer == null) {
                    result.rejectValue("lecturerId", "error.lecturerId", "Selected lecturer does not exist");
                    model.addAttribute("listLecturers", lecturerService.getAllLecturers());
                    model.addAttribute("home_view", "user/createUser.html");
                    return "dashboard";
                }
            }

            User newUser = new User(form.getUsername().trim(), form.getPassword(), normalizedRole, lecturer);
            boolean success = userService.addNewUser(newUser);

            if (success) {
                session.setAttribute("message", "User '" + form.getUsername() + "' created successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to create user. Please try again.");
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

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
    }
}
