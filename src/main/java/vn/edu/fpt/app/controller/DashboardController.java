package vn.edu.fpt.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboardHome() {
        return "redirect:/home";
    }

    @GetMapping("/dashboard/student")
    public String studentDashboard(Model model) {
        model.addAttribute("view", "student/student");
        return "layout/dashboard";
    }

    @GetMapping("/dashboard/course")
    public String courseDashboard(Model model) {
        model.addAttribute("view", "course/course");
        return "layout/dashboard";
    }
}
