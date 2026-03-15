/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.EnrollmentService;
import vn.edu.fpt.app.service.StudentService;
import java.util.List;
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
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final StudentService stuService;
    private final EnrollmentService enrService;
    private final DepartmentService depService;

    @Autowired
    public CourseController(
            CourseService courseService,
            StudentService stuService,
            EnrollmentService enrService,
            DepartmentService depService) {
        this.courseService = courseService;
        this.stuService = stuService;
        this.enrService = enrService;
        this.depService = depService;
    }

    @GetMapping
    public String list(Model model) {
        List<Course> listCourse = courseService.getAllCourses();
        List<Student> studentList = stuService.getAllStudents();
        List<Enrollment> enrollList = enrService.getAllEnrollments();

        model.addAttribute("totalCourses", listCourse.size());
        model.addAttribute("totalStudents", studentList.size());
        model.addAttribute("totalEnrolls", enrollList.size());
        model.addAttribute("listCourse", listCourse);
        model.addAttribute("home_view", "course.html");
        return "dashboard";
    }


    @GetMapping(params = "action=create")
    public String showCreate(Model model) {
        model.addAttribute("listDepartment", depService.getAllDepartments());
        model.addAttribute("home_view", "createCourse.html");
        return "dashboard";
    }

    @GetMapping(params = "action=update")
    public String showUpdate(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("home_view", "editCourse.html");
        return "dashboard";
    }

    @GetMapping(params = "action=delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("home_view", "deleteCourse.html");
        return "dashboard";
    }

    @PostMapping(params = "action=create")
    public String create(@ModelAttribute("course") CourseForm form) {
        if (form.getCode() == null || form.getTitle() == null || form.getCredits() == null || form.getDepartmentCode() == null) {
            return "redirect:/course";
        }
        Department d = new Department();
        d.setCode(form.getDepartmentCode());
        Course newCourse = new Course(0, form.getCode(), form.getTitle(), form.getCredits(), d);
        courseService.insertCourse(newCourse);
        return "redirect:/course";
    }

    @PostMapping(params = "action=update")
    public String update(@ModelAttribute("course") CourseForm form) {
        if (form.getId() == null || form.getCode() == null || form.getTitle() == null || form.getCredits() == null || form.getDepartmentCode() == null) {
            return "redirect:/course";
        }
        Department d = new Department();
        d.setCode(form.getDepartmentCode());
        Course updated = new Course(form.getId(), form.getCode(), form.getTitle(), form.getCredits(), d);
        courseService.updateCourse(updated);
        return "redirect:/course";
    }

    @PostMapping(params = "action=delete")
    public String delete(@ModelAttribute("course") CourseForm form) {
        if (form.getId() != null) {
            courseService.deleteCourse(form.getId());
        }
        return "redirect:/course";
    }

    public static class CourseForm {
        private Integer id;
        private String code;
        private String title;
        private Integer credits;
        private String departmentCode;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Integer getCredits() { return credits; }
        public void setCredits(Integer credits) { this.credits = credits; }
        public String getDepartmentCode() { return departmentCode; }
        public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
    }
}


