/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import java.util.List;

import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.AssessmentService;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
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

        model.addAttribute("totalCourse", courseList.size());
        model.addAttribute("totalAssessment", assessList.size());
        model.addAttribute("totalDepartment", departmentList.size());
        model.addAttribute("totalStudent", studentList.size());

        model.addAttribute("listAssessment", assessList);
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("allCourse", courseList);
        model.addAttribute("home_view", "assessment.html");
        return "dashboard";
    }

    @GetMapping(params = "action=create")
    public String showCreate(@RequestParam(name = "courseId", required = false) String courseIdParam, Model model) {
        model.addAttribute("allCourse", courseService.getAllCourses());
        if (courseIdParam != null && !courseIdParam.isEmpty()) {
            try {
                model.addAttribute("preSelectedCourseId", Integer.parseInt(courseIdParam));
            } catch (NumberFormatException ignored) {
            }
        }
        model.addAttribute("home_view", "createAssessment.html");
        return "dashboard";
    }

    @GetMapping(params = "action=edit")
    public String showEdit(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("allCourse", courseService.getAllCourses());
        model.addAttribute("assessment", assessService.getAssessmentById(id));
        model.addAttribute("home_view", "editAssessment.html");
        return "dashboard";
    }

    @GetMapping(params = "action=delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("allCourse", courseService.getAllCourses());
        model.addAttribute("assessment", assessService.getAssessmentById(id));
        model.addAttribute("home_view", "deleteAssessment.html");
        return "dashboard";
    }

    @PostMapping(params = "action=create")
    public String create(@ModelAttribute("assessment") AssessmentForm form) {
        if (form.getType() != null && form.getWeight() != null && form.getCourseId() != null) {
            assessService.createAssessment(form.getType(), form.getWeight(), form.getCourseId());
        }
        return "redirect:/assessment";
    }

    @PostMapping(params = "action=edit")
    public String update(@ModelAttribute("assessment") AssessmentForm form) {
        if (form.getId() != null && form.getType() != null && form.getWeight() != null && form.getCourseId() != null) {
            assessService.updateAssessment(form.getId(), form.getType(), form.getWeight(), form.getCourseId());
        }
        return "redirect:/assessment";
    }

    @PostMapping(params = "action=delete")
    public String delete(@ModelAttribute("assessment") AssessmentForm form) {
        if (form.getId() != null) {
            assessService.deleteAssessmentById(form.getId());
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


