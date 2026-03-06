package vn.edu.fpt.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboardHome(Model model) {

        // ===== DATA CHO HOME DASHBOARD =====
        model.addAttribute("view", "dashboard/home");

        model.addAttribute("totalCourses", 12);
        model.addAttribute("totalStudents", 240);
        model.addAttribute("totalLecturers", 18);
        model.addAttribute("totalDepartment", 6);

        // TẠM THỜI CHƯA CÓ SERVICE
        model.addAttribute("semesterList", java.util.List.of());
        model.addAttribute("Studentlist", java.util.List.of());
        model.addAttribute("listCourse", java.util.List.of());

        return "layout/dashboard";
    }

    @GetMapping("/dashboard/student")
    public String studentDashboard(Model model) {
        model.addAttribute("view", "dashboard/student");
        return "layout/dashboard";
    }

    @GetMapping("/dashboard/course")
    public String courseDashboard(Model model) {
        model.addAttribute("view", "dashboard/course");
        return "layout/dashboard";
    }
}
