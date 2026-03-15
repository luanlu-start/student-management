/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.app.dao.CourseDAO;
import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Course;

public class AssessmentDAO extends DBContext {

    private CourseDAO coursedao = new CourseDAO();

    //Update - XÃ“A THAM Sá» name
    public Boolean updateAssessment(String type, double weight, int courseId, int id) {
        try {
            String sql = "UPDATE [dbo].[Assessments]\n"
                    + "   SET [type] = ?\n"
                    + "      ,[weight] = ?\n"
                    + "      ,[courseId] = ?\n"
                    + " WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, type);
            ps.setDouble(2, weight);
            ps.setInt(3, courseId);
            ps.setInt(4, id);
            int result = ps.executeUpdate();
            if (result != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Create - XÃ“A THAM Sá» name
    public Boolean createAssessment(String type, double weight, int courseId) {
        try {
            String sql = "INSERT INTO [dbo].[Assessments]\n"
                    + "           ([type]\n"
                    + "           ,[weight]\n"
                    + "           ,[courseId])\n"
                    + "     VALUES\n"
                    + "           (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, type);
            ps.setDouble(2, weight);
            ps.setInt(3, courseId);
            int result = ps.executeUpdate();
            if (result != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Read
    public List<Assessment> getAllAssessment() {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT Assessments.id, Assessments.type, "
                + "Assessments.weight, Assessments.courseId "
                + "FROM Assessments INNER JOIN "
                + "Courses ON Assessments.courseId = Courses.id";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                double weight = rs.getDouble("weight");
                int courseId = rs.getInt("courseId");
                Course course = coursedao.getCourseById(courseId);
                Assessment assessment = new Assessment(id, type, weight, course);
                list.add(assessment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Assessment getAssessmentById(int id) {
        String sql = "SELECT Assessments.id, Assessments.type, "
                + "Assessments.weight, Assessments.courseId "
                + "FROM Assessments INNER JOIN "
                + "Courses ON Assessments.courseId = Courses.id WHERE Assessments.id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                double weight = rs.getDouble("weight");
                int courseId = rs.getInt("courseId");
                Course course = coursedao.getCourseById(courseId);
                return new Assessment(id, type, weight, course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Delete
    public Boolean deleteAssessmentById(int id) {
        try {
            String sql = "DELETE FROM [dbo].[Assessments] WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

   public List<Assessment> getAssessmentsByCourseId(int courseId) {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT * FROM Assessments WHERE courseId = ? ORDER BY id";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                double weight = rs.getDouble("weight");
                              
                Course course = coursedao.getCourseById(courseId); 
                Assessment assessment = new Assessment(id, type, weight, course);
                list.add(assessment);
            }
        } catch (Exception e) {
            System.out.println("Fail to get assessments by Course ID: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        AssessmentDAO dao = new AssessmentDAO();
        List<Assessment> list = dao.getAllAssessment();
        for (Assessment assessment : list) {
            System.out.println(assessment);
        }
    }
}

