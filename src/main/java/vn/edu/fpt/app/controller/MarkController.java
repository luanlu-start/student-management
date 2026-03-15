/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Mark;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.service.AssessmentService;
import vn.edu.fpt.app.service.ClassService;
import vn.edu.fpt.app.service.EnrollmentService;
import vn.edu.fpt.app.service.MarkService;

@Controller
@RequestMapping("/mark")
public class MarkController {

    private final MarkService markService;
    private final EnrollmentService enrollService;
    private final AssessmentService assessService;
    private final ClassService classService;

    @Autowired
    public MarkController(
            MarkService markService,
            EnrollmentService enrollService,
            AssessmentService assessService,
            ClassService classService) {
        this.markService = markService;
        this.enrollService = enrollService;
        this.assessService = assessService;
        this.classService = classService;
    }

    @GetMapping(params = "action=create")
    public String showCreate(
            @RequestParam(name = "classId", required = false) String classId,
            @RequestParam(name = "enrollId", required = false) String enrollIdParam,
            @RequestParam(name = "assessmentId", required = false) String assessIdParam,
            Model model) {
        model.addAttribute("allEnrollment", enrollService.getAllEnrollments());
        model.addAttribute("allAssessment", assessService.getAllAssessments());
        model.addAttribute("classId", classId);

        if (enrollIdParam != null && !enrollIdParam.isEmpty()) {
            try {
                model.addAttribute("preSelectedEnrollId", Integer.parseInt(enrollIdParam));
            } catch (NumberFormatException ignored) {
            }
        }

        if (assessIdParam != null && !assessIdParam.isEmpty()) {
            try {
                model.addAttribute("preSelectedAssessId", Integer.parseInt(assessIdParam));
            } catch (NumberFormatException ignored) {
            }
        }

        model.addAttribute("home_view", "createMark.html");
        return "dashboard";
    }

    @GetMapping(params = "action=edit")
    public String showEdit(
            @RequestParam(name = "classId", required = false) String classId,
            @RequestParam(name = "enrollId") String enrollIdParam,
            @RequestParam(name = "assessmentId") String assessIdParam,
            Model model) {
        try {
            int enrollId = Integer.parseInt(enrollIdParam);
            int assessmentId = Integer.parseInt(assessIdParam);
            Mark mark = markService.getMarkById(enrollId, assessmentId);
            model.addAttribute("mark", mark);
            model.addAttribute("classId", classId);
            model.addAttribute("home_view", "editMark.html");
            return "dashboard";
        } catch (Exception e) {
            return "redirect:/classes";
        }
    }

    @GetMapping(params = "action=delete")
    public String showDelete(
            @RequestParam(name = "classId", required = false) String classId,
            @RequestParam(name = "enrollId") String enrollIdParam,
            @RequestParam(name = "assessmentId") String assessIdParam,
            Model model) {
        try {
            int enrollIdDelete = Integer.parseInt(enrollIdParam);
            int assessmentIdDelete = Integer.parseInt(assessIdParam);
            Mark markDelete = markService.getMarkById(enrollIdDelete, assessmentIdDelete);
            model.addAttribute("mark", markDelete);
            model.addAttribute("classId", classId);
            model.addAttribute("home_view", "deleteMark.html");
            return "dashboard";
        } catch (Exception e) {
            return "redirect:/classes";
        }
    }

    @GetMapping(params = "action=viewByClass")
    public String viewByClass(@RequestParam(name = "classId") String classId, Model model) {
        try {
            int vClassId = Integer.parseInt(classId);
            Classes classInfo = classService.getClassById(vClassId);
            if (classInfo == null) {
                return "redirect:/classes";
            }

            List<Student> studentList = classService.getStudentsByClassId(vClassId);
            List<Assessment> assessmentList = assessService.getAssessmentsByCourseId(classInfo.getCourse().getId());
            Map<String, Mark> marksMap = markService.getMarksMapForClass(vClassId);
            Map<Integer, Integer> studentEnrollMap = enrollService.getStudentEnrollmentMap(vClassId);

            model.addAttribute("classInfo", classInfo);
            model.addAttribute("studentList", studentList);
            model.addAttribute("assessmentList", assessmentList);
            model.addAttribute("marksMap", marksMap);
            model.addAttribute("studentEnrollMap", studentEnrollMap);
            model.addAttribute("home_view", "viewClassMarks.html");
            return "dashboard";
        } catch (Exception e) {
            return "redirect:/classes";
        }
    }

    @PostMapping(params = "action=create")
    public String create(@ModelAttribute("markForm") MarkForm form, HttpSession session) {
        String redirectURL = "redirect:/mark?action=viewByClass&classId=" + (form.getClassId() == null ? "" : form.getClassId());
        try {
            int enrollId = Integer.parseInt(form.getEnrollId());
            int assessmentId = Integer.parseInt(form.getAssessmentId());
            double markValue = Double.parseDouble(form.getMark());

            if (markService.markExists(enrollId, assessmentId)) {
                session.setAttribute("error", "Mark already exists for this enrollment and assessment!");
                String createUrl = "redirect:/mark?action=create";
                if (form.getClassId() != null && !form.getClassId().isEmpty()) {
                    createUrl += "&classId=" + form.getClassId();
                }
                createUrl += "&enrollId=" + enrollId + "&assessmentId=" + assessmentId;
                return createUrl;
            }

            boolean created = markService.createMark(enrollId, assessmentId, markValue);
            session.setAttribute(created ? "success" : "error", created ? "Mark created successfully!" : "Failed to create mark!");
        } catch (Exception e) {
            session.setAttribute("error", "Invalid data for creating mark.");
        }
        return redirectURL;
    }

    @PostMapping(params = "action=edit")
    public String edit(@ModelAttribute("markForm") MarkForm form, HttpSession session) {
        String redirectURL = "redirect:/mark?action=viewByClass&classId=" + (form.getClassId() == null ? "" : form.getClassId());
        try {
            int enrollIdEdit = Integer.parseInt(form.getEnrollId());
            int assessmentIdEdit = Integer.parseInt(form.getAssessmentId());
            double markValueEdit = Double.parseDouble(form.getMark());

            boolean updated = markService.updateMark(enrollIdEdit, assessmentIdEdit, markValueEdit);
            session.setAttribute(updated ? "success" : "error", updated ? "Mark updated successfully!" : "Failed to update mark!");
        } catch (Exception e) {
            session.setAttribute("error", "Invalid data for editing mark.");
        }
        return redirectURL;
    }

    @PostMapping(params = "action=delete")
    public String delete(@ModelAttribute("markForm") MarkForm form, HttpSession session) {
        String redirectURL = "redirect:/mark?action=viewByClass&classId=" + (form.getClassId() == null ? "" : form.getClassId());
        try {
            int enrollIdDelete = Integer.parseInt(form.getEnrollId());
            int assessmentIdDelete = Integer.parseInt(form.getAssessmentId());

            boolean deleted = markService.deleteMark(enrollIdDelete, assessmentIdDelete);
            session.setAttribute(deleted ? "success" : "error", deleted ? "Mark deleted successfully!" : "Failed to delete mark!");
        } catch (Exception e) {
            session.setAttribute("error", "Invalid data for deleting mark.");
        }
        return redirectURL;
    }

    public static class MarkForm {
        private String classId;
        private String enrollId;
        private String assessmentId;
        private String mark;

        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
        public String getEnrollId() { return enrollId; }
        public void setEnrollId(String enrollId) { this.enrollId = enrollId; }
        public String getAssessmentId() { return assessmentId; }
        public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
        public String getMark() { return mark; }
        public void setMark(String mark) { this.mark = mark; }
    }
}

