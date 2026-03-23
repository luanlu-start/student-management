/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.app.dto.ClassDTO;
import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.Semester;
import vn.edu.fpt.app.entities.User;
import vn.edu.fpt.app.repository.ClassesRepository;
import vn.edu.fpt.app.service.ClassService;
import vn.edu.fpt.app.service.CourseService;
import vn.edu.fpt.app.service.LecturerService;
import vn.edu.fpt.app.service.SemesterService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

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
    private final ClassesRepository classesRepository;

    public ClassController(ClassService classService, CourseService courseService, LecturerService lecturerService,
                           SemesterService semesterService, ClassesRepository classesRepository) {
        this.classService = classService;
        this.courseService = courseService;
        this.lecturerService = lecturerService;
        this.semesterService = semesterService;
        this.classesRepository = classesRepository;
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
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable("id") int id, Model model) {

        Classes cls = classesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CLASS_NOT_FOUND"));
        ClassDTO dto = new ClassDTO();
        dto.setClassId(cls.getId());
        dto.setCode(cls.getCode());
        dto.setRoom(cls.getRoom());
        dto.setSchedule(cls.getSchedule());
        dto.setCourseId(cls.getCourse().getId());
        dto.setCourseTitle(cls.getCourse().getTitle());
        dto.setLecturerId(cls.getLecturer().getId());
        dto.setSemesterId(cls.getSemester().getId());

        model.addAttribute("classDTO", dto);
        model.addAttribute("listCourses", courseService.getAllCourses());
        model.addAttribute("listLecturers", lecturerService.getAllLecturers());
        model.addAttribute("listSemesters", semesterService.getAllSemesters());
        model.addAttribute("home_view", "class/editClass.html");

        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @GetMapping("/add")
    public String showCreate(Model model) {
        model.addAttribute("classDTO", new ClassDTO());
        model.addAttribute("listCourse", courseService.getAllCourses());
        model.addAttribute("listLecturer", lecturerService.getAllLecturers());
        model.addAttribute("listSemester", semesterService.getAllSemesters());
        model.addAttribute("home_view", "class/createClass.html");
        return "dashboard";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/delete")
    public String delete(@ModelAttribute("classDTO") ClassDTO dto,
                         RedirectAttributes redirect) {

        try {
            if (dto.getClassId() == null) {
                throw new RuntimeException("INVALID_ID");
            }

            classService.deleteClassById(dto.getClassId());

            redirect.addFlashAttribute("message", "Class deleted successfully!");
            redirect.addFlashAttribute("messageType", "success");

        } catch (RuntimeException e) {

            if ("CLASS_NOT_FOUND".equals(e.getMessage())) {
                redirect.addFlashAttribute("message", "Class not found!");
            } else if ("INVALID_ID".equals(e.getMessage())) {
                redirect.addFlashAttribute("message", "Invalid class ID!");
            } else {
                redirect.addFlashAttribute("message", "Failed to delete class!");
            }

            redirect.addFlashAttribute("messageType", "error");
        }

        return "redirect:/classes";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("classDTO") ClassDTO dto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> System.out.println(e));
        }
        // 1. Lỗi validation annotation
        if (result.hasErrors()) {
            model.addAttribute("home_view", "class/editClass.html");
            model.addAttribute("listCourses", courseService.getAllCourses());
            model.addAttribute("listLecturers", lecturerService.getAllLecturers());
            model.addAttribute("listSemesters", semesterService.getAllSemesters());
            return "dashboard";
        }

        try {
            // 2. Gọi service
            classService.updateClass(dto.getClassId(), dto);

            redirect.addFlashAttribute("message", "Class updated successfully!");
            redirect.addFlashAttribute("messageType", "success");
            return "redirect:/classes";

        } catch (RuntimeException e) {

            // 3. Map lỗi từ service
            if ("CLASS_NOT_FOUND".equals(e.getMessage())) {
                result.reject("error", "Class not found");
            } else if ("CLASS_CODE_EXISTS".equals(e.getMessage())) {
                result.rejectValue("code", "error.code", "Class code already exists");
            } else if ("COURSE_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("courseId", "error.courseId", "Course not found");
            } else if ("LECTURER_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("lecturerId", "error.lecturerId", "Lecturer not found");
            } else if ("SEMESTER_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("semesterId", "error.semesterId", "Semester not found");
            } else {
                model.addAttribute("ErrorMsg", "Update failed!");
            }

            // 4. Load lại data cho form
            model.addAttribute("home_view", "class/editClass.html");
            model.addAttribute("listCourses", courseService.getAllCourses());
            model.addAttribute("listLecturers", lecturerService.getAllLecturers());
            model.addAttribute("listSemesters", semesterService.getAllSemesters());

            return "dashboard";
        }
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("classDTO") ClassDTO dto,
                      BindingResult result,
                      Model model,
                      RedirectAttributes redirect) {

        // Nếu annotation validation lỗi
        if (result.hasErrors()) {
            model.addAttribute("home_view", "class/createClass.html");
            model.addAttribute("listCourse", courseService.getAllCourses());
            model.addAttribute("listLecturer", lecturerService.getAllLecturers());
            model.addAttribute("listSemester", semesterService.getAllSemesters());
            return "dashboard";
        }

        try {
            // Gọi service (nó sẽ ném exception nếu có lỗi)
            classService.insertClass(dto);

            redirect.addFlashAttribute("message", "New class '" + dto.getCode() + "' created successfully!");
            redirect.addFlashAttribute("messageType", "success");
            return "redirect:/classes";

        } catch (RuntimeException e) {
            // Map lỗi service ra BindingResult nếu liên quan đến field
            if ("CLASS_CODE_EXISTS".equals(e.getMessage())) {
                result.rejectValue("code", "error.code", "Class code already exists");
            } else if ("COURSE_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("courseId", "error.courseId", "Course not found");
            } else if ("LECTURER_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("lecturerId", "error.lecturerId", "Lecturer not found");
            } else if ("SEMESTER_NOT_FOUND".equals(e.getMessage())) {
                result.rejectValue("semesterId", "error.semesterId", "Semester not found");
            } else {
                model.addAttribute("ErrorMsg", "Failed to create class!");
            }

            // Gửi lại danh sách dropdown để render form
            model.addAttribute("listCourse", courseService.getAllCourses());
            model.addAttribute("listLecturer", lecturerService.getAllLecturers());
            model.addAttribute("listSemester", semesterService.getAllSemesters());
            model.addAttribute("home_view", "class/createClass.html");
            return "dashboard";
        }
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


}
