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
import java.sql.Date;
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

    @GetMapping("/edit")
    public String showEdit(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("departmentList", depService.getAllDepartments());
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("home_view", "student/editStudent.html");
        return "dashboard";
    }

    @GetMapping("/delete")
    public String showDelete(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("home_view", "student/deleteStudent.html");
        return "dashboard";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("depList", depService.getAllDepartments());
        model.addAttribute("home_view", "student/createStudent.html");
        return "dashboard";
    }

    @GetMapping("/view")
    public String showView(@RequestParam(name = "id") Integer id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("home_view", "student/viewStudent.html");
        return "dashboard";
    }

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

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/edit")
    public String edit(@ModelAttribute("studentForm") StudentForm form, HttpSession session) {
        if (form.getStudentId() == null || form.getBirthday() == null) {
            session.setAttribute("message", "Failed to update student!");
            session.setAttribute("messageType", "error");
            return "redirect:/student";
        }
        Department department = depService.getDepartmentByCode(form.getDepCode());
        Student updatedStudent = new Student(
                form.getStudentId(),
                form.getStudentName(),
                Date.valueOf(form.getBirthday()),
                form.getGender(),
                form.getAddress(),
                form.getCity(),
                department,
                form.getEmail(),
                form.getPhone());
        boolean update = studentService.updateStudent(updatedStudent);

        if (update) {
            session.setAttribute("message", "Student updated successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to update student!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/student";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/delete")
    public String delete(@ModelAttribute("studentForm") StudentForm form, HttpSession session) {
        if (form.getStudentId() == null) {
            return "redirect:/student";
        }
        Boolean deleteSuccess = studentService.deleteStudentById(form.getStudentId());
        if (deleteSuccess) {
            session.setAttribute("message", "Student deleted successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to delete student!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/student";
    }

    @PreAuthorize("hasAnyRole('admin', 'academic_staff')")
    @PostMapping("/add")
    public String add(@ModelAttribute("studentForm") StudentForm form, HttpSession session) {
        if (form.getBirthdate() == null) {
            return "redirect:/student";
        }
        String departmentCode = form.getDepCode();
        if (departmentCode == null || departmentCode.trim().isEmpty()) {
            departmentCode = form.getDepartment();
        }
        Department department = depService.getDepartmentByCode(departmentCode);
        Student newStudent = new Student(
                form.getStudentName(),
                Date.valueOf(form.getBirthdate()),
                form.getGender(),
                form.getAddress(),
                form.getCity(),
                department,
                form.getEmail(),
                form.getPhone());
        boolean addSuccess = studentService.insertNewStudent(newStudent);

        if (addSuccess) {
            session.setAttribute("message", "Student add successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to add student!");
            session.setAttribute("messageType", "error");
        }
        return "redirect:/student";
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

    public static class StudentForm {
        private Integer studentId;
        private String studentName;
        private String birthday;
        private String birthdate;
        private String gender;
        private String address;
        private String city;
        private String depCode;
        private String department;
        private String email;
        private String phone;

        public Integer getStudentId() { return studentId; }
        public void setStudentId(Integer studentId) { this.studentId = studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getBirthday() { return birthday; }
        public void setBirthday(String birthday) { this.birthday = birthday; }
        public String getBirthdate() { return birthdate; }
        public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getDepCode() { return depCode; }
        public void setDepCode(String depCode) { this.depCode = depCode; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }

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
