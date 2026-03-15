/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.Semester;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.EnrollmentService;
import vn.edu.fpt.app.service.LecturerService;
import vn.edu.fpt.app.service.SemesterService;
import vn.edu.fpt.app.service.StudentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Legion
 */
@Controller
public class HomeController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final LecturerService lecturerService;
    private final SemesterService semesterService;
    private final DepartmentService departmentService;

    @Autowired
    public HomeController(
            CourseService courseService,
            StudentService studentService,
            EnrollmentService enrollmentService,
            LecturerService lecturerService,
            SemesterService semesterService,
            DepartmentService departmentService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.lecturerService = lecturerService;
        this.semesterService = semesterService;
        this.departmentService = departmentService;
    }

    /**
     * Render dashboard home page with summary counters.
     */
    @GetMapping("/home")
    public String home(Model model) {

        List<Course> listCourse = courseService.getAllCourses();
        List<Student> studentList = studentService.getAllStudents();
        List<Enrollment> enrollList = enrollmentService.getAllEnrollments();
        List<Lecturer> lecturerList = lecturerService.getAllLecturers();
        List<Department> departmentList = departmentService.getAllDepartments();
        List<Semester> semesterList = semesterService.getAllSemesters();

        model.addAttribute("totalDepartment", departmentList.size());
        model.addAttribute("totalLecturers", lecturerList.size());
        model.addAttribute("totalCourses", listCourse.size());
        model.addAttribute("totalStudents", studentList.size());
        model.addAttribute("totalEnrolls", enrollList.size());

        model.addAttribute("lecturerList", lecturerList);
        model.addAttribute("listCourse", listCourse);
        model.addAttribute("Studentlist", studentList);
        model.addAttribute("semesterList", semesterList);
        model.addAttribute("home_view", "home");
        return "dashboard";
    }

}

