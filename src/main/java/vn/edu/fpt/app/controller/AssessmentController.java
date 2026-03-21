/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.AssessmentService;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Legion
 */
@Controller
@RequestMapping("/assessment")
@PreAuthorize("hasAnyRole('admin', 'academic_staff')")
public class AssessmentController {

    private final DepartmentService depService;
    private final StudentService stuService;
    private final AssessmentService assessService;
    private final CourseService courseService;

    @Autowired
    public AssessmentController(
            DepartmentService depService,
            StudentService stuService,
            AssessmentService assessService,
            CourseService courseService) {
        this.depService = depService;
        this.stuService = stuService;
        this.assessService = assessService;
        this.courseService = courseService;
    }

    @GetMapping
    public String list(Model model) {
        List<Assessment> assessList = assessService.getAllAssessments();
        List<Department> departmentList = depService.getAllDepartments();
        List<Student> studentList = stuService.getAllStudents();
        List<Course> courseList = courseService.getAllCourses();

        Map<Integer, List<Assessment>> assessmentsByCourse = new HashMap<>();
        for (Course course : courseList) {
            assessmentsByCourse.put(course.getId(), assessService.getAssessmentsByCourseId(course.getId()));
        }

        model.addAttribute("totalCourse", courseList.size());
        model.addAttribute("totalAssessment", assessList.size());
        model.addAttribute("totalDepartment", departmentList.size());
        model.addAttribute("totalStudent", studentList.size());

        model.addAttribute("listAssessment", assessList);
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("allCourse", courseList);
        model.addAttribute("assessmentsByCourse", assessmentsByCourse);
        model.addAttribute("home_view", "assessment/assessment.html");
        return "dashboard";
    }

    @GetMapping("/create")
    public String showCreate(@RequestParam(name = "courseId", required = false) String courseIdParam, Model model) {
        model.addAttribute("allCourse", courseService.getAllCourses());
        if (courseIdParam != null && !courseIdParam.isEmpty()) {
            try {
                model.addAttribute("preSelectedCourseId", Integer.parseInt(courseIdParam));
            } catch (NumberFormatException ignored) {
            }
        }
        model.addAttribute("home_view", "assessment/createAssessment.html");
        return "dashboard";
    }

    @GetMapping("/edit")
    public String showEdit(@RequestParam(name = "id") Integer id, Model model, HttpSession session) {
        Assessment assessment = assessService.getAssessmentById(id);
        if (assessment == null) {
            session.setAttribute("message", "Assessment not found!");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }
        model.addAttribute("allCourse", courseService.getAllCourses());
        model.addAttribute("assessment", assessment);
        model.addAttribute("home_view", "assessment/editAssessment.html");
        return "dashboard";
    }

    @GetMapping("/delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model, HttpSession session) {
        Assessment assessment = assessService.getAssessmentById(id);
        if (assessment == null) {
            session.setAttribute("message", "Assessment not found!");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }
        model.addAttribute("allCourse", courseService.getAllCourses());
        model.addAttribute("assessment", assessment);
        model.addAttribute("home_view", "assessment/deleteAssessment.html");
        return "dashboard";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("assessment") AssessmentForm form, HttpSession session) {
        if (form.getType() == null || form.getWeight() == null || form.getCourseId() == null) {
            session.setAttribute("message", "Please fill all required fields.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment/create";
        }

        double currentTotal = assessService.getAssessmentsByCourseId(form.getCourseId())
                .stream()
                .mapToDouble(Assessment::getWeight)
                .sum();
        if (currentTotal + form.getWeight() > 100.0) {
            session.setAttribute("message", "Total assessment weight for this course cannot exceed 100%.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment/create?courseId=" + form.getCourseId();
        }

        boolean created = assessService.createAssessment(form.getType(), form.getWeight(), form.getCourseId());
        if (created) {
            session.setAttribute("message", "Assessment created successfully!");
            session.setAttribute("messageType", "success");
            return "redirect:/assessment";
        }

        session.setAttribute("message", "Failed to create assessment!");
        session.setAttribute("messageType", "error");
        return "redirect:/assessment/create";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("assessment") AssessmentForm form, HttpSession session) {
        if (form.getId() == null || form.getType() == null || form.getWeight() == null || form.getCourseId() == null) {
            session.setAttribute("message", "Please fill all required fields.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }

        Assessment current = assessService.getAssessmentById(form.getId());
        if (current == null) {
            session.setAttribute("message", "Assessment not found!");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }

        double recalculatedTotal = assessService.getAssessmentsByCourseId(form.getCourseId())
                .stream()
                .filter(a -> a.getId() != form.getId())
                .mapToDouble(Assessment::getWeight)
                .sum() + form.getWeight();
        if (recalculatedTotal > 100.0) {
            session.setAttribute("message", "Total assessment weight for this course cannot exceed 100%.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment/edit?id=" + form.getId();
        }

        boolean updated = assessService.updateAssessment(form.getId(), form.getType(), form.getWeight(), form.getCourseId());
        if (updated) {
            session.setAttribute("message", "Assessment updated successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to update assessment!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/assessment";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("assessment") AssessmentForm form, HttpSession session) {
        if (form.getId() == null) {
            session.setAttribute("message", "Assessment id is required.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }

        boolean deleted = assessService.deleteAssessmentById(form.getId());
        if (deleted) {
            session.setAttribute("message", "Assessment deleted successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to delete assessment!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/assessment";
    }

    public static class AssessmentForm {
        private Integer id;
        private String type;
        private Double weight;
        private Integer courseId;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Double getWeight() { return weight; }
        public void setWeight(Double weight) { this.weight = weight; }
        public Integer getCourseId() { return courseId; }
        public void setCourseId(Integer courseId) { this.courseId = courseId; }
    }
}

