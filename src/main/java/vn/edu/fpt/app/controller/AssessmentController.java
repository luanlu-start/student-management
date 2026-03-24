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
 * Controller responsible for managing Assessments.
 *
 * Responsibilities:
 * - List assessments and provide summary counts
 * - Show create/edit/delete pages
 * - Validate total assessment weight per course (must not exceed 100%) when creating or updating
 *
 * Access:
 * - Restricted to 'admin' and 'academic_staff' roles.
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

    /**
     * GET /assessment
     * Show overview dashboard for assessments including counts and lists.
     */
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

    /**
     * GET /assessment/create
     * Show form to create a new assessment.
     * Optional query param: courseId - pre-select course on the create form.
     */
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

    /**
     * GET /assessment/edit?id={id}
     * Show edit page for an existing assessment. If the assessment is not found,
     * redirects back to /assessment with an error message.
     */
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

    /**
     * GET /assessment/delete?id={id}
     * Show confirmation page to delete an assessment.
     */
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

    /**
     * POST /assessment/create
     * Create a new assessment after validating required fields and ensuring
     * the total weight for the course does not exceed 100%.
     */
    @PostMapping("/create")
    public String create(@ModelAttribute("assessment") Assessment assessment, HttpSession session) {
        if (assessment.getType() == null || assessment.getCourse() == null || assessment.getCourse().getId() <= 0) {
            session.setAttribute("message", "Please fill all required fields.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment/create";
        }

        int courseId = assessment.getCourse().getId();

        double currentTotal = assessService.getAssessmentsByCourseId(courseId)
                .stream()
                .mapToDouble(Assessment::getWeight)
                .sum();
        if (currentTotal + assessment.getWeight() > 100.0) {
            session.setAttribute("message", "Total assessment weight for this course cannot exceed 100%.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment/create?courseId=" + courseId;
        }

        boolean created = assessService.createAssessment(assessment.getType(), assessment.getWeight(), courseId);
        if (created) {
            session.setAttribute("message", "Assessment created successfully!");
            session.setAttribute("messageType", "success");
            return "redirect:/assessment";
        }

        session.setAttribute("message", "Failed to create assessment!");
        session.setAttribute("messageType", "error");
        return "redirect:/assessment/create";
    }

    /**
     * POST /assessment/edit
     * Update an existing assessment after validating fields and ensuring the
     * recalculated total weight does not exceed 100%.
     */
    @PostMapping("/edit")
    public String update(@ModelAttribute("assessment") Assessment assessment, HttpSession session) {
        if (assessment.getId() <= 0 || assessment.getType() == null || assessment.getCourse() == null || assessment.getCourse().getId() <= 0) {
            session.setAttribute("message", "Please fill all required fields.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }

        int assessmentId = assessment.getId();
        int courseId = assessment.getCourse().getId();

        Assessment current = assessService.getAssessmentById(assessmentId);
        if (current == null) {
            session.setAttribute("message", "Assessment not found!");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }

        double recalculatedTotal = assessService.getAssessmentsByCourseId(courseId)
                .stream()
                .filter(a -> a.getId() != assessmentId)
                .mapToDouble(Assessment::getWeight)
                .sum() + assessment.getWeight();
        if (recalculatedTotal > 100.0) {
            session.setAttribute("message", "Total assessment weight for this course cannot exceed 100%.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment/edit?id=" + assessmentId;
        }

        boolean updated = assessService.updateAssessment(assessmentId, assessment.getType(), assessment.getWeight(), courseId);
        if (updated) {
            session.setAttribute("message", "Assessment updated successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to update assessment!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/assessment";
    }

    /**
     * POST /assessment/delete
     * Delete an assessment by id provided in the bound form.
     */
    @PostMapping("/delete")
    public String delete(@ModelAttribute("assessment") Assessment assessment, HttpSession session) {
        if (assessment.getId() <= 0) {
            session.setAttribute("message", "Assessment id is required.");
            session.setAttribute("messageType", "error");
            return "redirect:/assessment";
        }

        boolean deleted = assessService.deleteAssessmentById(assessment.getId());
        if (deleted) {
            session.setAttribute("message", "Assessment deleted successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to delete assessment!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/assessment";
    }

}

