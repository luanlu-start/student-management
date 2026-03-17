/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.Semester;
import vn.edu.fpt.app.entities.User;
import vn.edu.fpt.app.service.ClassService;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.LecturerService;
import vn.edu.fpt.app.service.SemesterService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
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
@RequestMapping("/classes")
public class ClassController {

    private final ClassService classService;
    private final CourseService courseService;
    private final LecturerService lecturerService;
    private final SemesterService semesterService;

    public ClassController(
            ClassService classService,
            CourseService courseService,
            LecturerService lecturerService,
            SemesterService semesterService) {
        this.classService = classService;
        this.courseService = courseService;
        this.lecturerService = lecturerService;
        this.semesterService = semesterService;
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff', 'lecturer')")
    @GetMapping
    public String list(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        String role = loginUser != null && loginUser.getRole() != null
                ? loginUser.getRole().trim().toLowerCase().replace(' ', '_')
                : "";

        List<Classes> classList;
        if (("lecturer".equals(role) || "teacher".equals(role))
                && loginUser != null && loginUser.getLecturer() != null) {
            classList = classService.getClassesByLecturerId(loginUser.getLecturer().getId());
        } else {
            classList = classService.getAllClasses();
        }

        List<Course> courseList = courseService.getAllCourses();
        List<Lecturer> lecturerList = lecturerService.getAllLecturers();
        List<Semester> semesterList = semesterService.getAllSemesters();

        model.addAttribute("totalClasses", classList.size());
        model.addAttribute("totalSemesters", semesterList.size());
        model.addAttribute("listClasses", classList);
        model.addAttribute("listCourses", courseList);
        model.addAttribute("listLecturers", lecturerList);
        model.addAttribute("listSemesters", semesterList);
        consumeMessage(session, model);
        model.addAttribute("home_view", "class/class.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff', 'lecturer')")
    @GetMapping("/view")
    public String view(@RequestParam(name = "id") Integer id, Model model, HttpSession session) {
        Classes cls = classService.getClassById(id);
        if (cls == null) {
            return "redirect:/classes";
        }

        User loginUser = (User) session.getAttribute("user");
        String role = loginUser != null && loginUser.getRole() != null
                ? loginUser.getRole().trim().toLowerCase().replace(' ', '_')
                : "";

        if (("lecturer".equals(role) || "teacher".equals(role))
                && (loginUser.getLecturer() == null
                || cls.getLecturer() == null
                || cls.getLecturer().getId() != loginUser.getLecturer().getId())) {
            session.setAttribute("message", "You can only view classes assigned to you.");
            session.setAttribute("messageType", "error");
            return "redirect:/classes";
        }

        model.addAttribute("classInfo", cls);
        model.addAttribute("studentList", classService.getStudentsByClassId(id));
        consumeMessage(session, model);
        model.addAttribute("home_view", "class/viewClass.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping("/delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model) {
        Classes deleteClass = classService.getClassById(id);
        if (deleteClass == null) {
            return "redirect:/classes";
        }
        model.addAttribute("classInfo", deleteClass);
        model.addAttribute("home_view", "class/deleteClass.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping("/edit")
    public String showEdit(@RequestParam(name = "id") Integer id, Model model) {
        Classes editClass = classService.getClassById(id);
        if (editClass == null) {
            return "redirect:/classes";
        }
        model.addAttribute("classInfo", editClass);
        model.addAttribute("lecturerList", lecturerService.getAllLecturers());
        model.addAttribute("semesterList", semesterService.getAllSemesters());
        model.addAttribute("home_view", "class/editClass.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping("/add")
    public String showCreate(Model model) {
        model.addAttribute("listCourses", courseService.getAllCourses());
        model.addAttribute("listLecturers", lecturerService.getAllLecturers());
        model.addAttribute("listSemesters", semesterService.getAllSemesters());
        model.addAttribute("home_view", "class/createClass.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/delete")
    public String delete(@ModelAttribute("classForm") ClassForm form, HttpSession session) {
        try {
            if (form.getClassId() == null) {
                throw new NumberFormatException();
            }
            boolean deleteSuccess = classService.deleteClassById(form.getClassId());
            session.setAttribute("message", deleteSuccess ? "Class deleted successfully!" : "Failed to delete class!");
            session.setAttribute("messageType", deleteSuccess ? "success" : "error");
        } catch (NumberFormatException e) {
            session.setAttribute("message", "Invalid class ID!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/classes";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/edit")
    public String edit(@ModelAttribute("classForm") ClassForm form, HttpSession session) {
        try {
            if (form.getClassId() == null || form.getSemesterId() == null || form.getLecturerId() == null) {
                throw new NumberFormatException();
            }
            boolean updateSuccess = classService.updateClass(
                    form.getClassId(),
                    form.getLecturerId(),
                    form.getSemesterId(),
                    form.getRoom(),
                    form.getSchedule());
            session.setAttribute("message", updateSuccess ? "Class updated successfully!" : "Failed to update class.");
            session.setAttribute("messageType", updateSuccess ? "success" : "error");
        } catch (NumberFormatException e) {
            session.setAttribute("message", "Invalid data provided. Update failed.");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/classes";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/add")
    public String add(@ModelAttribute("classForm") ClassForm form, HttpSession session) {
        try {
            if (form.getCourseId() == null || form.getLecturerId() == null || form.getSemesterId() == null || form.getCode() == null) {
                throw new IllegalArgumentException();
            }
            boolean success = classService.addNewClass(
                    form.getCode(),
                    form.getCourseId(),
                    form.getLecturerId(),
                    form.getSemesterId(),
                    form.getRoom(),
                    form.getSchedule());
            session.setAttribute("message", success ? "New class '" + form.getCode() + "' created successfully!" : "Failed to create class. (Class code might already exist).");
            session.setAttribute("messageType", success ? "success" : "error");
        } catch (Exception e) {
            session.setAttribute("message", "Error creating class: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        return "redirect:/classes";
    }

    private void consumeMessage(HttpSession session, Model model) {
        Object message = session.getAttribute("message");
        Object messageType = session.getAttribute("messageType");

        if (message != null) {
            model.addAttribute("message", message);
            model.addAttribute("messageType", messageType != null ? messageType : "info");
            session.removeAttribute("message");
            session.removeAttribute("messageType");
        }
    }

    public static class ClassForm {
        private Integer classId;
        private Integer courseId;
        private Integer lecturerId;
        private Integer semesterId;
        private String code;
        private String room;
        private String schedule;

        public Integer getClassId() { return classId; }
        public void setClassId(Integer classId) { this.classId = classId; }
        public Integer getCourseId() { return courseId; }
        public void setCourseId(Integer courseId) { this.courseId = courseId; }
        public Integer getLecturerId() { return lecturerId; }
        public void setLecturerId(Integer lecturerId) { this.lecturerId = lecturerId; }
        public Integer getSemesterId() { return semesterId; }
        public void setSemesterId(Integer semesterId) { this.semesterId = semesterId; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }
        public String getSchedule() { return schedule; }
        public void setSchedule(String schedule) { this.schedule = schedule; }
    }

}
