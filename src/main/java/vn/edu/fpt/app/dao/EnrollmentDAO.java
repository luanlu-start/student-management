/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.edu.fpt.app.dao.ClassDAO;
import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.dao.SemesterDAO;
import vn.edu.fpt.app.dao.StudentDAO;
import vn.edu.fpt.app.entities.*;

/**
 *
 * @author Legion
 */
public class EnrollmentDAO extends DBContext {

    private StudentDAO studentdao = new StudentDAO();
    private vn.edu.fpt.app.dao.SemesterDAO semesterDAO = new SemesterDAO();
    private vn.edu.fpt.app.dao.ClassDAO clsDAO = new ClassDAO();

    //READ
    public List<Enrollment> getAllEnrollment() {
        List<Enrollment> list = new ArrayList<>();
        final String sql = "SELECT * FROM Enrollments";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Student student = studentdao.getStudentById(rs.getInt("studentId"));
                Classes cls = clsDAO.getClassById(rs.getInt("classId"));

                Enrollment enrollment = new Enrollment(id, student, cls);
                list.add(enrollment);

            }
        } catch (SQLException e) {
            System.out.println("Fail to get all Enrollment:" + e.getMessage());
        }
        return list;
    }

    public Enrollment getEnrollmentById(int id) {
        final String sql = "SELECT * FROM Enrollments WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student student = studentdao.getStudentById(rs.getInt("studentId"));
                Classes cls = clsDAO.getClassById(rs.getInt("classId"));

                Enrollment enrollment = new Enrollment(id, student, cls);
                return enrollment;

            }
        } catch (SQLException e) {
            System.out.println("Fail to get Enrollment by ID:" + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        EnrollmentDAO dao = new EnrollmentDAO();
        List<Enrollment> list = dao.getAllEnrollment();
        for (Enrollment e : list) {
            System.out.println(e);
        }
//
//        Enrollment e = dao.getEnrollmentById(3);
//        System.out.println(e);

//        List<Course> courseList = dao.getEnrollmentCourseByStudentID(6);
//        for (Course course : courseList) {
//            System.out.println(course);
//        }
    }

    //CREATE
    public Boolean createEnrollment(int studentId, int classId) {
        try {
            String sql = "insert into Enrollments(studentId, classId)\n"
                    + "values (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, classId);
            int result = ps.executeUpdate();
            if (result != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<Integer, Integer> getStudentEnrollmentMap(int classId) {
        Map<Integer, Integer> map = new HashMap<>();
        String sql = "SELECT id, studentId FROM Enrollments WHERE classId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("studentId"), rs.getInt("id"));
            }
        } catch (Exception e) {
            System.out.println("Failed to get student enrollment map: " + e.getMessage());
        }
        return map;
    }

//    //Update
//    public Boolean updateEnrollment(int studentId, int courseId, int semesterId, int id) {
//        try {
//            String sql = "update Enrollments\n"
//                    + "set studentId = ?, courseId = ?, semesterId = ?\n"
//                    + "where id = ?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, studentId);
//            ps.setInt(2, courseId);
//            ps.setInt(3, semesterId);
//            ps.setInt(4, id);
//            int result = ps.executeUpdate();
//            if (result != 0) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    //Delete
    public Boolean deleteEnrollment(int studentId, int classId) {
        try {
            String sql = "Delete from Enrollments where studentId = ? and classId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, classId);
            int result = ps.executeUpdate();
            if (result != 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}

