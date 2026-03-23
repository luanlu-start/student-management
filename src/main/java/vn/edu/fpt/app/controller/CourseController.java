/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.app.dto.CourseDTO;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.EnrollmentService;
import vn.edu.fpt.app.service.StudentService;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping
    public String list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "departmentCode", required = false) String departmentCode,
            Model model) {
        List<Course> allCourses = courseService.getAllCourses();
        List<Student> studentList = stuService.getAllStudents();
        List<Enrollment> enrollList = enrService.getAllEnrollments();

        String normalizedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        String normalizedDepartmentCode = departmentCode == null ? "" : departmentCode.trim();
        if ("all".equalsIgnoreCase(normalizedDepartmentCode)) {
            normalizedDepartmentCode = "";
        }
        final String filterKeyword = normalizedKeyword;
        final String filterDepartmentCode = normalizedDepartmentCode;

        List<Course> filteredCourses = allCourses.stream()
                .filter(course -> {
                    if (filterKeyword.isEmpty()) {
                        return true;
                    }
                    String code = course.getCode() == null ? "" : course.getCode().toLowerCase();
                    String title = course.getTitle() == null ? "" : course.getTitle().toLowerCase();
                    return code.contains(filterKeyword) || title.contains(filterKeyword);
                })
                .filter(course -> {
                    if (filterDepartmentCode.isEmpty()) {
                        return true;
                    }
                    return course.getDepartment() != null
                            && course.getDepartment().getCode() != null
                            && filterDepartmentCode.equalsIgnoreCase(course.getDepartment().getCode());
                })
                .toList();

        model.addAttribute("totalCourses", allCourses.size());
        model.addAttribute("totalStudents", studentList.size());
        model.addAttribute("totalEnrolls", enrollList.size());
        model.addAttribute("listCourse", filteredCourses);
        model.addAttribute("listDepartment", depService.getAllDepartments());
        model.addAttribute("keyword", keyword == null ? "" : keyword.trim());
        model.addAttribute("departmentCode", normalizedDepartmentCode);
        model.addAttribute("home_view", "course/course.html");
        return "dashboard";
    }


    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping("/create")
    public String showCreate(Model model) {

        model.addAttribute("course", new CourseDTO());

        model.addAttribute("listDepartment", depService.getAllDepartments());
        model.addAttribute("home_view", "course/createCourse.html");

        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping("/update")
    public String showUpdate(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "error", required = false) String error,
            Model model) {
        Course course = courseService.getCourseById(id);
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setTitle(course.getTitle());
        dto.setCredits(course.getCredits());
        dto.setDepartmentCode(course.getDepartment().getCode());
        model.addAttribute("course", dto);
        model.addAttribute("listDepartment", depService.getAllDepartments());

        if ("department".equalsIgnoreCase(error)) {
            model.addAttribute("errorMsg", "Department code is invalid. Please choose an existing department.");
        } else if ("update".equalsIgnoreCase(error)) {
            model.addAttribute("errorMsg", "Failed to update course. Please try again.");
        }

        model.addAttribute("home_view", "course/editCourse.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping("/delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("home_view", "course/deleteCourse.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("course") CourseDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {

        // validate annotation
        if (result.hasErrors()) {
            model.addAttribute("home_view", "course/createCourse.html");
            return "dashboard";
        }

        try {
            courseService.insertCourse(dto);

            redirect.addFlashAttribute("message", "Create successful!");
            redirect.addFlashAttribute("messageType", "success");
            return "redirect:/course";

        } catch (RuntimeException e) {

            if ("CODE_EXISTS".equals(e.getMessage())) {
                result.rejectValue("code", "error.code", "Course code already exists");
            } else if ("DEPARTMENT_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("departmentCode", "error.departmentCode", "Department not found");
            } else {
                model.addAttribute("ErrorMsg", "Create failed!");
            }
            model.addAttribute("listDepartment", depService.getAllDepartments());
            model.addAttribute("home_view", "course/createCourse.html");
            return "dashboard";
        }
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("course") CourseDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {

        // validate annotation
        if (result.hasErrors()) {
            model.addAttribute("home_view", "course/editCourse.html");
            return "dashboard";
        }

        try {
            courseService.updateCourse(dto);

            redirect.addFlashAttribute("message", "Update successful!");
            redirect.addFlashAttribute("messageType", "success");
            return "redirect:/course";

        } catch (RuntimeException e) {

            if ("CODE_EXISTS".equals(e.getMessage())) {
                result.rejectValue("code", "error.code", "Course code already exists");
            } else if ("DEPARTMENT_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("departmentCode", "error.departmentCode", "Department not found");
            } else if ("NOT_FOUND".equals(e.getMessage())) {
                redirect.addFlashAttribute("message", "Course not found!");
                redirect.addFlashAttribute("messageType", "error");
                return "redirect:/course";
            } else {
                model.addAttribute("ErrorMsg", "Update failed!");
            }
            model.addAttribute("listDepartment", depService.getAllDepartments());
            model.addAttribute("home_view", "course/editCourse.html");
            return "dashboard";
        }
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id,
                         RedirectAttributes redirect) {

        try {
            courseService.deleteCourse(id);

            redirect.addFlashAttribute("message", "Delete successful!");
            redirect.addFlashAttribute("messageType", "success");

        } catch (RuntimeException e) {

            if ("NOT_FOUND".equals(e.getMessage())) {
                redirect.addFlashAttribute("message", "Course not found!");
            } else {
                redirect.addFlashAttribute("message", "Delete failed!");
            }

            redirect.addFlashAttribute("messageType", "error");
        }

        return "redirect:/course";
    }

}
