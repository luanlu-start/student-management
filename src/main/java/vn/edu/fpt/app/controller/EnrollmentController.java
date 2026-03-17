package vn.edu.fpt.app.controller;

import java.util.List;

import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.AssessmentService;
import vn.edu.fpt.app.service.ClassService;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.EnrollmentService;
import vn.edu.fpt.app.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/enrollment")
@PreAuthorize("hasAnyRole('admin', 'academic_staff')")
public class EnrollmentController {

    private final DepartmentService depService;
    private final EnrollmentService enrollService;
    private final StudentService stuService;
    private final ClassService classService;
    private final AssessmentService assessService;
    private final CourseService courseService;

    @Autowired
    public EnrollmentController(
            DepartmentService depService,
            EnrollmentService enrollService,
            StudentService stuService,
            ClassService classService,
            AssessmentService assessService,
            CourseService courseService) {
        this.depService = depService;
        this.enrollService = enrollService;
        this.stuService = stuService;
        this.classService = classService;
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
        model.addAttribute("home_view", "enrollment/enrollment.html");
        return "dashboard";
    }

    @GetMapping("/addStudent")
    public String showAddStudent(@RequestParam(name = "classId", required = false) String classIdParam, Model model) {
        model.addAttribute("allStudent", stuService.getAllStudents());
        model.addAttribute("allClass", classService.getAllClasses());
        if (classIdParam != null && !classIdParam.isEmpty()) {
            try {
                model.addAttribute("preSelectedClassId", Integer.parseInt(classIdParam));
            } catch (NumberFormatException ignored) {
            }
        }
        model.addAttribute("home_view", "enrollment/createEnrollment.html");
        return "dashboard";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute("enrollmentForm") EnrollmentForm form, HttpSession session) {
        if (form.getStudentId() == null || form.getClassId() == null) {
            session.setAttribute("message", "Invalid data. Please select both student and class.");
            session.setAttribute("messageType", "error");
            return "redirect:/classes";
        }

        int studentId = form.getStudentId();
        int classId = form.getClassId();

        if (enrollService.isAlreadyEnrolled(studentId, classId)) {
            Student student = stuService.getStudentById(studentId);
            String studentName = student != null ? student.getName() : "Student #" + studentId;
            session.setAttribute("message", "\"" + studentName + "\" is already enrolled in this class!");
            session.setAttribute("messageType", "error");
            return "redirect:/classes/view?id=" + classId;
        }

        boolean success = enrollService.createEnrollment(studentId, classId);
        if (success) {
            Student student = stuService.getStudentById(studentId);
            String studentName = student != null ? student.getName() : "Student #" + studentId;
            session.setAttribute("message", "\"" + studentName + "\" has been added to the class successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to add student to class. Please try again.");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/classes/view?id=" + classId;
    }

    @PostMapping("/deleteStudent")
    public String showDeleteStudent(@ModelAttribute("enrollmentForm") EnrollmentForm form, Model model) {
        if (form.getStudentId() == null || form.getClassId() == null) {
            return "redirect:/classes";
        }
        Student student = stuService.getStudentById(form.getStudentId());
        Classes classes = classService.getClassById(form.getClassId());
        model.addAttribute("classes", classes);
        model.addAttribute("student", student);
        model.addAttribute("home_view", "enrollment/deleteEnrollment.html");
        return "dashboard";
    }

    @PostMapping("/confirmDeleteStudent")
    public String confirmDeleteStudent(@ModelAttribute("enrollmentForm") EnrollmentForm form, HttpSession session) {
        if (form.getStudentId() == null || form.getClassId() == null) {
            return "redirect:/classes";
        }

        int studentId = form.getStudentId();
        int classId = form.getClassId();

        Student student = stuService.getStudentById(studentId);
        String studentName = student != null ? student.getName() : "Student #" + studentId;

        boolean success = enrollService.deleteEnrollment(studentId, classId);
        if (success) {
            session.setAttribute("message", "\"" + studentName + "\" has been removed from the class.");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to remove student from class.");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/classes/view?id=" + classId;
    }

    public static class EnrollmentForm {
        private Integer studentId;
        private Integer classId;

        public Integer getStudentId() { return studentId; }
        public void setStudentId(Integer studentId) { this.studentId = studentId; }
        public Integer getClassId() { return classId; }
        public void setClassId(Integer classId) { this.classId = classId; }
    }
}

