/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.StudentService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that handles student-related views and actions.
 *
 * Responsibilities:
 * - List, filter and paginate students
 * - Show add / edit / delete / view student pages
 * - Handle form submissions for creating, updating and deleting students
 *
 * Access:
 * - Restricted to users with roles 'admin' or 'academic_staff' via @PreAuthorize
 */
@Controller
@RequestMapping("/student")
@PreAuthorize("hasAnyRole('admin', 'academic_staff')")
public class StudentController {

    private final StudentService studentService;
    private final DepartmentService depService;

    @Autowired
    public StudentController(StudentService studentService, DepartmentService depService) {
        this.studentService = studentService;
        this.depService = depService;
    }

    /**
     * GET /student
     * Display paginated list of students.
     * Optional query param: i - page index (1-based). If missing or invalid defaults to 1.
     */
    @GetMapping
    public String list(@RequestParam(name = "i", required = false) String i, Model model, HttpSession session) {
        int pagSize = 10;
        int pagIndex = 1;

        if (i != null) {
            try {
                pagIndex = Integer.parseInt(i);
            } catch (NumberFormatException e) {
                pagIndex = 1;
            }
        }

        model.addAttribute("filter", false);
        List<Student> list = studentService.getAllStudents();
        List<Student> listPag = pagination(list, pagIndex, pagSize);

        model.addAttribute("listStd", listPag);
        model.addAttribute("page", pagIndex);
        model.addAttribute("totalPage", (list.size() + pagSize - 1) / pagSize);
        model.addAttribute("listDepartmet", depService.getAllDepartments());
        consumeMessage(session, model);
        model.addAttribute("home_view", "student/student.html");
        return "dashboard";
    }

    /**
     * GET /student/edit?id={id}
     * Show edit form for a student. Expects a valid student id.
     */
    @GetMapping("/edit")
    public String showEdit(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("departmentList", depService.getAllDepartments());
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("home_view", "student/editStudent.html");
        return "dashboard";
    }

    /**
     * GET /student/delete?id={id}
     * Show confirmation page to delete a student.
     */
    @GetMapping("/delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("home_view", "student/deleteStudent.html");
        return "dashboard";
    }

    /**
     * GET /student/add
     * Show form to create a new student.
     */
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("depList", depService.getAllDepartments());
        model.addAttribute("home_view", "student/createStudent.html");
        return "dashboard";
    }

    /**
     * GET /student/view?id={id}
     * Show detailed view of a student.
     */
    @GetMapping("/view")
    public String showView(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("home_view", "student/viewStudent.html");
        return "dashboard";
    }

    /**
     * GET /student/fillter
     * Filter students by name and/or department.
     * Query params: i (page index), nameStudent, departmentname.
     */
    @GetMapping("/fillter")
    public String filter(
            @RequestParam(name = "i", required = false) String i,
            @RequestParam(name = "nameStudent", required = false) String studentName,
            @RequestParam(name = "departmentname", required = false) String departmentName,
            Model model,
            HttpSession session) {
        int pagSize = 10;
        int pagIndex = 1;
        if (i != null) {
            try {
                pagIndex = Integer.parseInt(i);
            } catch (NumberFormatException e) {
                pagIndex = 1;
            }
        }

        model.addAttribute("filter", true);
        model.addAttribute("listDepartmet", depService.getAllDepartments());

        if (studentName == null || studentName.trim().isEmpty()) {
            studentName = "";
        }
        if (departmentName == null || departmentName.equalsIgnoreCase("all")) {
            departmentName = "";
        }

        List<Student> filteredList;
        if (!departmentName.isEmpty() && !studentName.isEmpty()) {
            filteredList = studentService.filterByBoth(departmentName, studentName);
        } else if (!departmentName.isEmpty()) {
            filteredList = studentService.filterByDepartmentCode(departmentName);
        } else if (!studentName.isEmpty()) {
            filteredList = studentService.filterByName(studentName);
        } else {
            filteredList = studentService.getAllStudents();
        }

        int totalPageFilter = (filteredList.size() + pagSize - 1) / pagSize;
        List<Student> listPagF = pagination(filteredList, pagIndex, pagSize);

        model.addAttribute("listStd", listPagF);
        model.addAttribute("page", pagIndex);
        model.addAttribute("totalPage", totalPageFilter);
        model.addAttribute("nameStudent", studentName);
        model.addAttribute("departmentname", departmentName);
        consumeMessage(session, model);
        model.addAttribute("home_view", "student/student.html");
        return "dashboard";
    }

    /**
     * POST /student/edit
     * Process edit form submission. Validates required fields and updates the student.
     */
    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/edit")
    public String edit(@ModelAttribute("studentForm") Student form, HttpSession session) {
        if (form.getId() <= 0 || form.getBirthdate() == null) {
            session.setAttribute("message", "Failed to update student!");
            session.setAttribute("messageType", "error");
            return "redirect:/student";
        }
        String departmentCode = form.getDepartment() != null ? form.getDepartment().getCode() : null;
        Department department = depService.getDepartmentByCode(departmentCode);
        form.setDepartment(department);
        boolean update = studentService.updateStudent(form);

        if (update) {
            session.setAttribute("message", "Student updated successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to update student!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/student";
    }

    /**
     * POST /student/delete
     * Delete a student by id provided in the bound form.
     */
    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/delete")
    public String delete(@ModelAttribute("studentForm") Student form, HttpSession session) {
        if (form.getId() <= 0) {
            return "redirect:/student";
        }
        Boolean deleteSuccess = studentService.deleteStudentById(form.getId());
        if (deleteSuccess) {
            session.setAttribute("message", "Student deleted successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to delete student!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/student";
    }

    /**
     * POST /student/add
     * Create a new student from form data. Validates required fields.
     */
    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/add")
    public String add(@ModelAttribute("studentForm") Student form, HttpSession session) {
        if (form.getBirthdate() == null) {
            return "redirect:/student";
        }
        String departmentCode = form.getDepartment() != null ? form.getDepartment().getCode() : null;
        Department department = depService.getDepartmentByCode(departmentCode);
        form.setDepartment(department);
        boolean addSuccess = studentService.insertNewStudent(form);

        if (addSuccess) {
            session.setAttribute("message", "Student add successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to add student!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/student";
    }

    /**
     * Helper to move flash messages from session to model and clear them.
     */
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


    /**
     * Simple in-memory pagination helper used by list and filter actions.
     * Returns sublist for the given pageIndex (1-based) and pageSize.
     */
    public List<Student> pagination(List<Student> list, int pageIndex, int pageSize) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        int begin = (pageIndex - 1) * pageSize;
        if (begin >= list.size()) {
            return new ArrayList<>();
        }
        int end = pageIndex * pageSize;
        if (end > list.size()) {
            end = list.size();
        }
        return list.subList(begin, end);
    }
}
