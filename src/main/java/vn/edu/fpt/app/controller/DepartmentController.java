/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.app.dto.DepartmentDTO;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.StudentService;
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
@RequestMapping("/department")
@PreAuthorize("hasAnyRole('admin', 'academic_staff')")
public class DepartmentController {

    private final DepartmentService depService;
    private final StudentService stuService;

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
        model.addAttribute("home_view", "department/department.html");
        return "dashboard";
    }


    @GetMapping("/edit")
    public String showEdit(@RequestParam("depCode") String depCode, Model model) {

        Department dep = depService.getDepartmentByCode(depCode);

        DepartmentDTO dto = new DepartmentDTO();
        dto.setCode(dep.getCode());
        dto.setName(dep.getName());
        dto.setHead(dep.getDepartmentHead());
        dto.setEmail(dep.getEmail());
        dto.setPhone(dep.getPhone());

        model.addAttribute("department", dto);
        model.addAttribute("home_view", "department/editDepartment.html");

        return "dashboard";
    }

    @GetMapping("/delete")
    public String showDelete(@RequestParam(name = "depCode") String depCode, Model model) {
        model.addAttribute("department", depService.getDepartmentByCode(depCode));
        model.addAttribute("home_view", "department/deleteDepartment.html");
        return "dashboard";
    }

    @GetMapping("/add")
    public String showCreate(Model model) {
        model.addAttribute("department", new DepartmentDTO());
        model.addAttribute("home_view", "department/createDepartment.html");
        return "dashboard";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("department") DepartmentDTO dto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {

        if (result.hasErrors()) {
            model.addAttribute("home_view", "department/editDepartment.html");
            return "dashboard";
        }

        try {
            depService.update(dto);

            redirect.addFlashAttribute("message", "Update successful!");
            redirect.addFlashAttribute("messageType", "success");
            return "redirect:/department";

        } catch (RuntimeException e) {

            String msg = e.getMessage();

            if ("NAME_EXISTS".equals(msg)) {
                result.rejectValue("name", "error.name", "Department name already exists");
            }
            else if ("NOT_FOUND".equals(msg)) {
                redirect.addFlashAttribute("message", "Department not found!");
                redirect.addFlashAttribute("messageType", "error");
                return "redirect:/department";
            } else {
                model.addAttribute("ErrorMsg", "Update failed!");
            }

            model.addAttribute("department", dto); // 🔥 giữ data
            model.addAttribute("home_view", "department/editDepartment.html");
            return "dashboard";
        }
    }
//
//    @PostMapping("/delete")
//    public String delete(@ModelAttribute("departmentForm") DepartmentForm form, HttpSession session) {
//        boolean deleteSuccess = form.getDepCode() != null && depService.deleteDepartmentByCode(form.getDepCode());
//        if (deleteSuccess) {
//            session.setAttribute("message", "Department deleted successfully!");
//            session.setAttribute("messageType", "success");
//        } else {
//            session.setAttribute("message", "Failed to delete department!");
//            session.setAttribute("messageType", "error");
//        }
//        return "redirect:/department";
//    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("department") DepartmentDTO dto,
                      BindingResult result,
                      RedirectAttributes redirect,
                      Model model) {

        // validate form
        if (result.hasErrors()) {
            model.addAttribute("home_view", "department/createDepartment.html");  // ✅ Set view
            return "dashboard";  // ✅ Return dashboard với home_view
        }

        try {
            depService.add(dto);

            redirect.addFlashAttribute("message", "Department added successfully!");
            redirect.addFlashAttribute("messageType", "success");
            return "redirect:/department";

        } catch (RuntimeException e) {

            if ("CODE_EXISTS".equals(e.getMessage())) {
                result.rejectValue("code", "error.code", "Code already exists");
            } else if ("NAME_EXISTS".equals(e.getMessage())) {
                result.rejectValue("name", "error.name", "Name already exists");
            }

            return "department/createDepartment";
        }
    }


}
