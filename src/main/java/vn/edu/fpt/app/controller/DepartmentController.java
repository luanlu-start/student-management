/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.StudentService;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService depService;
    private final StudentService stuService;

    @Autowired
    public DepartmentController(DepartmentService depService, StudentService stuService) {
        this.depService = depService;
        this.stuService = stuService;
    }

    @GetMapping
    public String list(Model model) {
        List<Department> departmentList = depService.getAllDepartments();
        List<Student> studentList = stuService.getAllStudents();

        model.addAttribute("totalDepartment", departmentList.size());
        model.addAttribute("totalStudent", studentList.size());
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("home_view", "department.html");
        return "dashboard";
    }


    @GetMapping(params = "action=edit")
    public String showEdit(@RequestParam(name = "depCode") String depCode, Model model) {
        model.addAttribute("editDepartment", depService.getDepartmentByCode(depCode));
        model.addAttribute("home_view", "editDepartment.html");
        return "dashboard";
    }

    @GetMapping(params = "action=delete")
    public String showDelete(@RequestParam(name = "depCode") String depCode, Model model) {
        model.addAttribute("department", depService.getDepartmentByCode(depCode));
        model.addAttribute("home_view", "deleteDepartment.html");
        return "dashboard";
    }

    @GetMapping(params = "action=add")
    public String showCreate(Model model) {
        model.addAttribute("home_view", "createDepartment.html");
        return "dashboard";
    }

    @PostMapping(params = "action=edit")
    public String edit(@ModelAttribute("departmentForm") DepartmentForm form, Model model) {
        if (form.getDepartmentCode() == null) {
            return "redirect:/department";
        }
        boolean update = depService.updateDepartmentByCode(
                form.getDepartmentCode(),
                form.getDepartmentName(),
                form.getDepartmentHead(),
                form.getEmail(),
                form.getPhone());
        if (update) {
            return "redirect:/department";
        }
        model.addAttribute("ErrorMsg", "Fail to update department!");
        model.addAttribute("editDepartment", depService.getDepartmentByCode(form.getDepartmentCode()));
        model.addAttribute("home_view", "department.html");
        return "dashboard";
    }

    @PostMapping(params = "action=delete")
    public String delete(@ModelAttribute("departmentForm") DepartmentForm form, HttpSession session) {
        boolean deleteSuccess = form.getDepCode() != null && depService.deleteDepartmentByCode(form.getDepCode());
        if (deleteSuccess) {
            session.setAttribute("message", "Department deleted successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to delete department!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/department";
    }

    @PostMapping(params = "action=add")
    public String add(@ModelAttribute("departmentForm") DepartmentForm form, HttpSession session) {
        if (form.getDepartmentCode() == null || form.getDepartmentName() == null) {
            return "redirect:/department";
        }
        Department department = new Department(
                form.getDepartmentCode(),
                form.getDepartmentName(),
                form.getDepartmentHead(),
                form.getPhone(),
                form.getEmail());
        boolean addSuccess = depService.addNewDepartment(department);
        if (addSuccess) {
            session.setAttribute("message", "Department add successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to add department!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/department";
    }

    public static class DepartmentForm {
        private String depCode;
        private String departmentCode;
        private String departmentName;
        private String departmentHead;
        private String email;
        private String phone;

        public String getDepCode() { return depCode; }
        public void setDepCode(String depCode) { this.depCode = depCode; }
        public String getDepartmentCode() { return departmentCode; }
        public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public String getDepartmentHead() { return departmentHead; }
        public void setDepartmentHead(String departmentHead) { this.departmentHead = departmentHead; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }

}


