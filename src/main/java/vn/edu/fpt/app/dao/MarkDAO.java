package vn.edu.fpt.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import vn.edu.fpt.app.dao.AssessmentDAO;
import vn.edu.fpt.app.dao.CourseDAO;
import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.dao.EnrollmentDAO;
import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.Mark;

/**
 * 
 * @author Legion
 */
public class MarkDAO extends DBContext {

    private vn.edu.fpt.app.dao.EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private vn.edu.fpt.app.dao.AssessmentDAO assessmentDAO = new AssessmentDAO();
    private CourseDAO courseDAO = new CourseDAO();

    // CREATE
    public Boolean createMark(int enrollId, int assessmentId, double mark) {
        String sql = "INSERT INTO [dbo].[Marks] ([enrollId], [assessmentId], [mark]) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollId);
            ps.setInt(2, assessmentId);
            ps.setDouble(3, mark);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Failed to create mark: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // READ - Get all marks
    public List<Mark> getAllMarks() {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT Marks.enrollId, Marks.assessmentId, Marks.mark "
                + "FROM Marks "
                + "INNER JOIN Enrollments ON Marks.enrollId = Enrollments.id "
                + "INNER JOIN Assessments ON Marks.assessmentId = Assessments.id";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int enrollId = rs.getInt("enrollId");
                int assessmentId = rs.getInt("assessmentId");
                double mark = rs.getDouble("mark");

                Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollId);
                Assessment assessment = assessmentDAO.getAssessmentById(assessmentId);

                Mark markObj = new Mark(enrollment, assessment, mark);
                list.add(markObj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get all marks: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // READ - Get mark by enrollId and assessmentId
    public Mark getMarkById(int enrollId, int assessmentId) {
        String sql = "SELECT Marks.enrollId, Marks.assessmentId, Marks.mark "
                + "FROM Marks "
                + "WHERE Marks.enrollId = ? AND Marks.assessmentId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollId);
            ps.setInt(2, assessmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double mark = rs.getDouble("mark");
                Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollId);
                Assessment assessment = assessmentDAO.getAssessmentById(assessmentId);
                return new Mark(enrollment, assessment, mark);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get mark by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // READ - Get all marks by enrollment ID
    public List<Mark> getMarksByEnrollmentId(int enrollId) {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT Marks.enrollId, Marks.assessmentId, Marks.mark "
                + "FROM Marks "
                + "WHERE Marks.enrollId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int assessmentId = rs.getInt("assessmentId");
                double mark = rs.getDouble("mark");

                Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollId);
                Assessment assessment = assessmentDAO.getAssessmentById(assessmentId);

                Mark markObj = new Mark(enrollment, assessment, mark);
                list.add(markObj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get marks by enrollment ID: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // READ - Get all marks by student ID
    public List<Mark> getMarksByStudentId(int studentId) {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT Marks.enrollId, Marks.assessmentId, Marks.mark "
                + "FROM Marks "
                + "INNER JOIN Enrollments ON Marks.enrollId = Enrollments.id "
                + "WHERE Enrollments.studentId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int enrollId = rs.getInt("enrollId");
                int assessmentId = rs.getInt("assessmentId");
                double mark = rs.getDouble("mark");

                Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollId);
                Assessment assessment = assessmentDAO.getAssessmentById(assessmentId);

                Mark markObj = new Mark(enrollment, assessment, mark);
                list.add(markObj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get marks by student ID: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // READ - Get all marks by assessment ID
    public List<Mark> getMarksByAssessmentId(int assessmentId) {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT Marks.enrollId, Marks.assessmentId, Marks.mark "
                + "FROM Marks "
                + "WHERE Marks.assessmentId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, assessmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int enrollId = rs.getInt("enrollId");
                double mark = rs.getDouble("mark");

                Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollId);
                Assessment assessment = assessmentDAO.getAssessmentById(assessmentId);

                Mark markObj = new Mark(enrollment, assessment, mark);
                list.add(markObj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get marks by assessment ID: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public Boolean updateMark(int enrollId, int assessmentId, double mark) {
        String sql = "UPDATE [dbo].[Marks] SET [mark] = ? WHERE [enrollId] = ? AND [assessmentId] = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, mark);
            ps.setInt(2, enrollId);
            ps.setInt(3, assessmentId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Failed to update mark: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public Boolean deleteMark(int enrollId, int assessmentId) {
        String sql = "DELETE FROM [dbo].[Marks] WHERE [enrollId] = ? AND [assessmentId] = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollId);
            ps.setInt(2, assessmentId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Failed to delete mark: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // DELETE - Delete all marks by enrollment ID
    public Boolean deleteMarksByEnrollmentId(int enrollId) {
        String sql = "DELETE FROM [dbo].[Marks] WHERE [enrollId] = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Failed to delete marks by enrollment ID: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Utility method - Check if mark exists
    public boolean markExists(int enrollId, int assessmentId) {
        String sql = "SELECT COUNT(*) FROM Marks WHERE enrollId = ? AND assessmentId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollId);
            ps.setInt(2, assessmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Failed to check mark existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public Map<String, Mark> getMarksMapForClass(int classId) {
        Map<String, Mark> marksMap = new HashMap<>();
        
        String sql = "SELECT e.studentId, m.assessmentId, m.mark, m.enrollId "
                + "FROM Marks m "
                + "JOIN Enrollments e ON m.enrollId = e.id "
                + "JOIN Classes cl ON e.classId = cl.id " // DÃ¹ng classId
                + "WHERE cl.id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int studentId = rs.getInt("studentId");
                int assessmentId = rs.getInt("assessmentId");
                double markValue = rs.getDouble("mark");
                int enrollId = rs.getInt("enrollId");
                
                Mark markObj = new Mark();
                markObj.setMark(markValue);
                
                Enrollment tempEnroll = new Enrollment();
                tempEnroll.setId(enrollId);
                markObj.setEnrollment(tempEnroll);
                
                String key = studentId + "_" + assessmentId;
                marksMap.put(key, markObj);
            }
        } catch (Exception e) {
            System.out.println("Fail to get marks map for class: " + e.getMessage());
        }
        return marksMap;
    }

    // Main method for testing
    public static void main(String[] args) {
        MarkDAO dao = new MarkDAO();

        System.out.println("=== All Marks ===");
        List<Mark> marks = dao.getAllMarks();
        for (Mark mark : marks) {
            System.out.println(mark);
        }

        System.out.println("\n=== Mark by ID (enrollId=1, assessmentId=1) ===");
        Mark mark = dao.getMarkById(1, 1);
        System.out.println(mark);

        // Test getMarksByStudentId
        System.out.println("\n=== Marks by Student ID (studentId=1) ===");
        List<Mark> studentMarks = dao.getMarksByStudentId(1);
        for (Mark m : studentMarks) {
            System.out.println(m);
        }

        // Test markExists
        System.out.println("\n=== Check if mark exists ===");
        boolean exists = dao.markExists(1, 1);
        System.out.println("Mark exists: " + exists);
    }
}

