/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.app.dao.*;
import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.dao.SemesterDAO;
import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.Semester;
import vn.edu.fpt.app.entities.Student;

/**
 *
 * @author Legion
 */
public class ClassDAO extends DBContext {

    private CourseDAO courseDAO = new CourseDAO();
    private vn.edu.fpt.app.dao.SemesterDAO semesterDAO = new SemesterDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private LecturerDAO lecturerDAO = new LecturerDAO();

    public List<Classes> getAllClass() {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT cl.id AS class_id, cl.code, cl.room, cl.schedule, c.id AS course_id, "
                + "l.id AS lecturer_id, s.id AS semester_id, "
                + "(SELECT COUNT(e.studentId) "
                + "FROM Enrollments e "
                + "WHERE e.classId = cl.id) AS student_count "
                + "FROM Classes cl "
                + "LEFT JOIN Courses c ON cl.courseId = c.id "
                + "LEFT JOIN Lecturers l ON cl.lecturerId = l.id "
                + "LEFT JOIN Semesters s ON cl.semesterId = s.id "
                + "LEFT JOIN Departments d ON c.departmentCode = d.Code ORDER BY c.code";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("class_id");
                String code = rs.getString("code");
                String room = rs.getString("room");
                String schedule = rs.getString("schedule");

                Course course = courseDAO.getCourseById(rs.getInt("course_id"));
                Lecturer lecturer = lecturerDAO.getLecturerById(rs.getInt("lecturer_id"));
                Semester semester = semesterDAO.getSemesterById(rs.getInt("semester_id"));
                int studentCount = rs.getInt("student_count");

                Classes classes = new Classes(id, code, room, schedule, course, lecturer, semester, studentCount);
                list.add(classes);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all class: " + e.getMessage());
        }
        return list;
    }

    public Classes getClassById(int id) {
        String sql = "SELECT cl.id AS class_id, cl.code, cl.room, cl.schedule, c.id AS course_id, "
                + "l.id AS lecturer_id, s.id AS semester_id, "
                + "(SELECT COUNT(e.studentId) "
                + "FROM Enrollments e "
                + "WHERE e.classId = cl.id) AS student_count "
                + "FROM Classes cl "
                + "LEFT JOIN Courses c ON cl.courseId = c.id "
                + "LEFT JOIN Lecturers l ON cl.lecturerId = l.id "
                + "LEFT JOIN Semesters s ON cl.semesterId = s.id "
                + "LEFT JOIN Departments d ON c.departmentCode = d.Code "
                + "WHERE cl.id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String code = rs.getString("code");
                String room = rs.getString("room");
                String schedule = rs.getString("schedule");

                Course course = courseDAO.getCourseById(rs.getInt("course_id"));
                Lecturer lecturer = lecturerDAO.getLecturerById(rs.getInt("lecturer_id"));
                Semester semester = semesterDAO.getSemesterById(rs.getInt("semester_id"));
                int studentCount = rs.getInt("student_count");

                Classes cls = new Classes(id, code, room, schedule, course, lecturer, semester, studentCount);
                return cls;
            }
        } catch (Exception e) {
            System.out.println("Fail to class by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Student> getStudentsByClassId(int classId) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.id FROM Students s "
                + "JOIN Enrollments e ON s.id = e.studentId "
                + "WHERE e.classId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");

                Student student = studentDAO.getStudentById(id);
                list.add(student);
            }

        } catch (Exception e) {
            System.out.println("Fail to get Students by Class ID: " + e.getMessage());
        }
        return list;
    }

    public boolean deleteClassById(int id) {
        String sql = "DELETE FROM Classes WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to delete class by ID: " + e.getMessage());
        }
        return false;
    }


    public boolean updateClass(int classId, int lecturerId, int semesterId, String room, String schedule) {
        String sql = "UPDATE Classes SET "
                + " lecturerId = ?, "
                + " semesterId = ?, "
                + " room = ?, "
                + " schedule = ? "
                + " WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lecturerId);
            ps.setInt(2, semesterId);
            ps.setString(3, room);
            ps.setString(4, schedule);
            ps.setInt(5, classId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to update class: " + e.getMessage());
            return false;
        }
    }

    public boolean addNewClass(String code, int courseId, int lecturerId, int semesterId, String room, String schedule) {
        String sql = "INSERT INTO Classes (code, courseId, lecturerId, semesterId, room, schedule)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.setInt(2, courseId);
            ps.setInt(3, lecturerId);
            ps.setInt(4, semesterId);
            ps.setString(5, room);
            ps.setString(6, schedule);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to create class: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        ClassDAO dao = new ClassDAO();
        List<Classes> list = dao.getAllClass();
        for (Classes c : list) {
            System.out.println(c);
        }

        List<Student> stdList = dao.getStudentsByClassId(15);
        dao.deleteClassById(15);
        for (Student student : stdList) {
            System.out.println(student);
        }
    }
}

