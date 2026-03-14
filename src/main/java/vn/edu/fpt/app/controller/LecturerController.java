package vn.edu.fpt.app.controller;


import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.LecturerService;

import java.awt.print.Pageable;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {
    private final LecturerService lecturerService;
    private final DepartmentService departmentService;

    public LecturerController(LecturerService lecturerService, DepartmentService departmentService) {
        this.lecturerService = lecturerService;
        this.departmentService = departmentService;
    }

    //list lecturer
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       Model model) {

        Page<Lecturer> lecturerPage =
                lecturerService.getAllLecturers(PageRequest.of(page, 12));

        model.addAttribute("lecturerPage", lecturerPage);
        model.addAttribute("view", "dashboard/lecturer/list");

        return "layout/dashboard";
    }

    //add form
    @GetMapping("/add")
    public String addForm(Model model) {
        Lecturer lecturer = new Lecturer();
        lecturer.setDepartment(new Department());

        model.addAttribute("lecturer", lecturer);
        model.addAttribute("listDepartments", departmentService.getAll());
        model.addAttribute("view", "dashboard/lecturer/form");
        return "layout/dashboard";
    }


    //save
    @PostMapping("save")
    public String save(@ModelAttribute("lecturer") Lecturer lecturer) {
        lecturerService.save(lecturer);
        return "redirect:/lecturer/list";
    }

    //edit
    @GetMapping("edit/{id}")
    public String editForm(@PathVariable("id") int id, Model model) {
        Lecturer lecturer = lecturerService.getById(id);

        if (lecturer.getDepartment() == null) {
            lecturer.setDepartment(new Department());
        }
        System.out.println(departmentService.getAll());
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("listDepartments", departmentService.getAll());
        model.addAttribute("view", "dashboard/lecturer/form");
        return "layout/dashboard";
    }


    //delete
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id) {
        lecturerService.delete(id);
        return "redirect:/lecturer/list";
    }

}
