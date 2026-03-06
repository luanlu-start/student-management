package vn.edu.fpt.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.service.StudentService;
import vn.edu.fpt.app.service.DepartmentService;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final DepartmentService departmentService;

    public StudentController(StudentService studentService,
                             DepartmentService departmentService) {
        this.studentService = studentService;
        this.departmentService = departmentService;
    }

    // ========= LIST =========
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("listStd", studentService.getAll());
        model.addAttribute("view", "dashboard/student/list");
        return "layout/dashboard";
    }

    // ========= ADD FORM =========
    @GetMapping("/add")
    public String addForm(Model model) {

        Student student = new Student();
        student.setDepartment(new Department()); // ⭐ bắt buộc

        model.addAttribute("student", student);
        model.addAttribute("listDepartments", departmentService.getAll());
        model.addAttribute("view", "dashboard/student/form");

        return "layout/dashboard";
    }


    // ========= SAVE =========
    @PostMapping("/save")
    public String save(@ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/student/list";
    }

    // ========= EDIT =========
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Student student = studentService.getById(id);

        if (student.getDepartment() == null) {
            student.setDepartment(new Department());
        }

        model.addAttribute("student", student);
        model.addAttribute("listDepartments", departmentService.getAll());
        model.addAttribute("view", "dashboard/student/form");

        return "layout/dashboard";
    }

    // ========= DELETE =========
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        studentService.delete(id);
        return "redirect:/student/list";
    }
}
