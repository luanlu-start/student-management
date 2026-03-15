/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.fpt.app.controller;

import vn.edu.fpt.app.entities.Semester;
import vn.edu.fpt.app.service.SemesterService;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
 *
 * @author Legion
 */
@Controller
@RequestMapping("/semester")
public class SemesterController {

    private final SemesterService semesterService;

    @Autowired
    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping
    public String list(Model model) {
        List<Semester> sesList = semesterService.getAllSemesters();
        model.addAttribute("totalSemester", sesList.size());
        model.addAttribute("semesterList", sesList);
        model.addAttribute("home_view", "semester.html");
        return "dashboard";
    }

    @GetMapping(params = "action=edit")
    public String showEdit(@RequestParam(name = "sesID") Integer sesId, Model model) {
        model.addAttribute("editSemester", semesterService.getSemesterById(sesId));
        model.addAttribute("home_view", "editSemester.html");
        return "dashboard";
    }

    @GetMapping(params = "action=delete")
    public String showDelete(@RequestParam(name = "sesID") Integer sesId, Model model) {
        model.addAttribute("delSemester", semesterService.getSemesterById(sesId));
        model.addAttribute("home_view", "deleteSemester.html");
        return "dashboard";
    }

    @GetMapping(params = "action=add")
    public String showCreate(Model model) {
        model.addAttribute("home_view", "createSemester.html");
        return "dashboard";
    }

    @PostMapping(params = "action=edit")
    public String edit(@ModelAttribute("semesterForm") SemesterForm form, HttpSession session, Model model) {
        if (form.getSemesterID() == null || form.getSemesterCode() == null || form.getSemesterYear() == null || form.getSemesterBegin() == null || form.getSemesterEnd() == null) {
            return "redirect:/semester";
        }
        Semester updated = new Semester(
                form.getSemesterID(),
                form.getSemesterCode(),
                form.getSemesterYear(),
                Date.valueOf(form.getSemesterBegin()),
                Date.valueOf(form.getSemesterEnd()));
        boolean ok = semesterService.updateSemester(updated);
        if (ok) {
            return "redirect:/semester";
        }
        model.addAttribute("ErrorMsg", "Fail to update semester!");
        model.addAttribute("editSemester", semesterService.getSemesterById(form.getSemesterID()));
        model.addAttribute("home_view", "semester.html");
        return "dashboard";
    }

    @PostMapping(params = "action=delete")
    public String delete(@ModelAttribute("semesterForm") SemesterForm form, HttpSession session) {
        if (form.getSemesterID() == null) {
            return "redirect:/semester";
        }
        boolean deleteSuccess = semesterService.deleteSemesterById(form.getSemesterID());
        session.setAttribute("message", deleteSuccess ? "Department deleted successfully!" : "Failed to delete department!");
        session.setAttribute("messageType", deleteSuccess ? "success" : "error");
        return "redirect:/semester";
    }

    @PostMapping(params = "action=add")
    public String add(@ModelAttribute("semesterForm") SemesterForm form, HttpSession session, Model model) {
        String prefix = trimOrEmpty(form.getCodePrefix());
        String yearStr = trimOrEmpty(form.getSemesterYear() == null ? null : String.valueOf(form.getSemesterYear()));
        String beginStr = trimOrEmpty(form.getSemesterBegin());
        String endStr = trimOrEmpty(form.getSemesterEnd());

        if (prefix.isEmpty() || yearStr.isEmpty() || beginStr.isEmpty() || endStr.isEmpty()) {
            model.addAttribute("ErrorMsg", "Please fill all required fields.");
            model.addAttribute("home_view", "createSemester.html");
            return "dashboard";
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
            if (year < 1997 || year > 2100) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            model.addAttribute("ErrorMsg", "Year must be a valid number (1997-2100).");
            model.addAttribute("home_view", "createSemester.html");
            return "dashboard";
        }

        String nb;
        String ne;
        try {
            nb = normalizeDateOrThrow(year, beginStr);
            ne = normalizeDateOrThrow(year, endStr);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("ErrorMsg", "Dates must be in MM-DD or YYYY-MM-DD (e.g. 05-10 or 2025-05-10).");
            model.addAttribute("home_view", "createSemester.html");
            return "dashboard";
        }

        Date beginDate;
        Date endDate;
        try {
            beginDate = Date.valueOf(nb);
            endDate = Date.valueOf(ne);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("ErrorMsg", "Invalid date value. Use YYYY-MM-DD (e.g. 2025-05-10).");
            model.addAttribute("home_view", "createSemester.html");
            return "dashboard";
        }

        if (endDate.before(beginDate)) {
            model.addAttribute("ErrorMsg", "End date must be after begin date!");
            model.addAttribute("home_view", "createSemester.html");
            return "dashboard";
        }

        LocalDate beginLD = beginDate.toLocalDate();
        LocalDate endLD = endDate.toLocalDate();
        LocalDate[] win = semesterWindow(year, prefix);

        if (beginLD.isBefore(win[0]) || endLD.isAfter(win[1])) {
            model.addAttribute("ErrorMsg", "Dates must fall within the " + prefix + " window (" + win[0] + " to " + win[1] + ").");
            model.addAttribute("home_view", "createSemester.html");
            return "dashboard";
        }

        long days = ChronoUnit.DAYS.between(beginLD, endLD) + 1;
        if (days < 90) {
            model.addAttribute("ErrorMsg", "A semester must be at least 90 days.");
            model.addAttribute("home_view", "createSemester.html");
            return "dashboard";
        }

        String code = prefix + String.format("%02d", year % 100);
        boolean ok = semesterService.addNewSemester(new Semester(code, year, beginDate, endDate));

        session.setAttribute("message", ok ? "Semester added successfully!" : "Failed to add semester!");
        session.setAttribute("messageType", ok ? "success" : "error");
        return "redirect:/semester";
    }

    public static class SemesterForm {
        private Integer semesterID;
        private String semesterCode;
        private Integer semesterYear;
        private String semesterBegin;
        private String semesterEnd;
        private String codePrefix;

        public Integer getSemesterID() { return semesterID; }
        public void setSemesterID(Integer semesterID) { this.semesterID = semesterID; }
        public String getSemesterCode() { return semesterCode; }
        public void setSemesterCode(String semesterCode) { this.semesterCode = semesterCode; }
        public Integer getSemesterYear() { return semesterYear; }
        public void setSemesterYear(Integer semesterYear) { this.semesterYear = semesterYear; }
        public String getSemesterBegin() { return semesterBegin; }
        public void setSemesterBegin(String semesterBegin) { this.semesterBegin = semesterBegin; }
        public String getSemesterEnd() { return semesterEnd; }
        public void setSemesterEnd(String semesterEnd) { this.semesterEnd = semesterEnd; }
        public String getCodePrefix() { return codePrefix; }
        public void setCodePrefix(String codePrefix) { this.codePrefix = codePrefix; }
    }

    private static String trimOrEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    private static String normalizeDateOrThrow(int year4, String input) {
        if (input == null) {
            throw new IllegalArgumentException("Null date string");
        }

        String v = input.replaceAll("\\s+", "")
                .replaceAll("[\\u2010-\\u2015\\u2212\\uFE58\\uFE63\\uFF0D/\\.]", "-");

        if (v.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            String[] p = v.split("-");
            int y = Integer.parseInt(p[0]);
            int m = Integer.parseInt(p[1]);
            int d = Integer.parseInt(p[2]);
            try {
                LocalDate.of(y, m, d);
            } catch (DateTimeException e) {
                throw new IllegalArgumentException("Invalid Y-M-D");
            }
            return String.format("%04d-%02d-%02d", y, m, d);
        }

        if (v.matches("\\d{1,2}-\\d{1,2}")) {
            String[] p = v.split("-");
            int m = Integer.parseInt(p[0]);
            int d = Integer.parseInt(p[1]);
            try {
                LocalDate.of(year4, m, d);
            } catch (DateTimeException e) {
                throw new IllegalArgumentException("Invalid M-D");
            }
            return String.format("%04d-%02d-%02d", year4, m, d);
        }

        throw new IllegalArgumentException("Bad date format: " + v);
    }

    private static LocalDate[] semesterWindow(int year, String prefix) {
        switch (prefix) {
            case "SP":
                return new LocalDate[]{LocalDate.of(year, 1, 1), LocalDate.of(year, 4, 30)};
            case "SU":
                return new LocalDate[]{LocalDate.of(year, 5, 1), LocalDate.of(year, 8, 31)};
            case "FA":
                return new LocalDate[]{LocalDate.of(year, 9, 1), LocalDate.of(year, 12, 31)};
            default:
                throw new IllegalArgumentException("Unknown prefix: " + prefix);
        }
    }

}


