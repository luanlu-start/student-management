/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.service.DepartmentService;
import vn.edu.fpt.app.service.LecturerService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
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
@RequestMapping("/lecturer")
@PreAuthorize("hasAnyRole('admin', 'academic_staff')")
public class LecturerController {

    private final LecturerService lecturerService;
    private final DepartmentService departmentService;

    public LecturerController(LecturerService lecturerService, DepartmentService departmentService) {
        this.lecturerService = lecturerService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "i", required = false) String i,
            Model model) {
        int pagSize = 10;
        int pagIndex = 1;

        if (i != null) {
            try {
                pagIndex = Integer.parseInt(i);
            } catch (NumberFormatException e) {
                pagIndex = 1;
            }
        }

        List<Lecturer> list = lecturerService.getAllLecturers();
        int totalPage = (list.size() + pagSize - 1) / pagSize;
        List<Lecturer> listPag = pagnigation(list, pagIndex, pagSize);

        model.addAttribute("filter", false);
        model.addAttribute("listLecturer", listPag);
        model.addAttribute("page", pagIndex);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("listDepartments", departmentService.getAllDepartments());
        model.addAttribute("home_view", "lecturer/lecturer.html");
        return "dashboard";
    }

    @GetMapping("/delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("lecturer", lecturerService.getLecturerById(id));
        model.addAttribute("home_view", "lecturer/deleteLecturer.html");
        return "dashboard";
    }

    @GetMapping("/edit")
    public String showEdit(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("departmentList", departmentService.getAllDepartments());
        model.addAttribute("lecturer", lecturerService.getLecturerById(id));
        model.addAttribute("home_view", "lecturer/editLecturer.html");
        return "dashboard";
    }

    @GetMapping("/view")
    public String showView(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("lecturer", lecturerService.getLecturerById(id));
        model.addAttribute("home_view", "lecturer/viewLecturer.html");
        return "dashboard";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("departmentList", departmentService.getAllDepartments());
        model.addAttribute("home_view", "lecturer/createLecturer.html");
        return "dashboard";
    }

    @GetMapping("/fillter")
    public String filter(
            @RequestParam(name = "i", required = false) String i,
            @RequestParam(name = "nameLecturer", required = false) String lecturerName,
            @RequestParam(name = "departmentCode", required = false) String depCode,
            Model model) {
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
        model.addAttribute("listDepartments", departmentService.getAllDepartments());

        if (lecturerName == null || lecturerName.trim().isEmpty()) {
            lecturerName = "";
        }
        if (depCode == null || depCode.equalsIgnoreCase("all") || depCode.isEmpty()) {
            depCode = "";
        }

        List<Lecturer> filteredList;
        if (!depCode.isEmpty() && !lecturerName.isEmpty()) {
            filteredList = lecturerService.filterByBoth(depCode, lecturerName);
        } else if (!depCode.isEmpty()) {
            filteredList = lecturerService.filterByDepartmentCode(depCode);
        } else if (!lecturerName.isEmpty()) {
            filteredList = lecturerService.filterByName(lecturerName);
        } else {
            filteredList = lecturerService.getAllLecturers();
            model.addAttribute("filter", false);
        }

        int totalPage = (filteredList.size() + pagSize - 1) / pagSize;
        List<Lecturer> listPagF = pagnigation(filteredList, pagIndex, pagSize);

        model.addAttribute("listLecturer", listPagF);
        model.addAttribute("page", pagIndex);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("nameLecturer", lecturerName);
        model.addAttribute("departmentCode", depCode);
        model.addAttribute("home_view", "lecturer/lecturer.html");
        return "dashboard";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("lecturerForm") Lecturer form, HttpSession session) {
        if (form.getId() <= 0) {
            return "redirect:/lecturer";
        }
        try {
            boolean deleteSuccess = lecturerService.deleteLecturerById(form.getId());
            if (deleteSuccess) {
                session.setAttribute("message", "Lecturer deleted successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to delete lecturer!");
                session.setAttribute("messageType", "error");
            }
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("FK_Classes_Lecturer")) {
                session.setAttribute("message", "Cannot delete lecturer. They are still assigned to a class.");
            } else {
                session.setAttribute("message", "Failed to delete lecturer: " + e.getMessage());
            }
            session.setAttribute("messageType", "error");
        }
        return "redirect:/lecturer";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("lecturerForm") Lecturer form, HttpSession session) {
        if (form.getId() <= 0) {
            return "redirect:/lecturer";
        }
        String depCode = form.getDepartment() != null ? form.getDepartment().getCode() : null;
        Department dep = departmentService.getDepartmentByCode(depCode);
        Lecturer eLecturer = new Lecturer(form.getId(), form.getName(), form.getEmail(), form.getPhone(), form.getTitle(), dep);
        boolean updateSuccess = lecturerService.updateLecturer(eLecturer);
        if (updateSuccess) {
            session.setAttribute("message", "Lecturer update successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to update lecturer!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/lecturer";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("lecturerForm") Lecturer form, HttpSession session) {
        String depCode = form.getDepartment() != null ? form.getDepartment().getCode() : null;
        Department dep = departmentService.getDepartmentByCode(depCode);
        Lecturer newLecturer = new Lecturer(form.getName(), form.getEmail(), form.getPhone(), form.getTitle(), dep);
        boolean addSuccess = lecturerService.addNewLecturer(newLecturer);
        if (addSuccess) {
            session.setAttribute("message", "Lecturer added successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to add new lecturer!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/lecturer";
    }


    public List<Lecturer> pagnigation(List<Lecturer> list, int pageIndex, int pagsize) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        int begin = (pageIndex - 1) * pagsize;

        if (begin >= list.size()) {
            return new ArrayList<>();
        }

        int end = pageIndex * pagsize;

        if (end > list.size()) {
            end = list.size();
        }

        return list.subList(begin, end);
    }

}
