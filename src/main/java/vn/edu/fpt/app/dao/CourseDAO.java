/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.dao.DepartmentDAO;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;

/**
 *
 * @author Legion
 */
public class CourseDAO extends DBContext {

    vn.edu.fpt.app.dao.DepartmentDAO depDAO = new DepartmentDAO();

    public List<Course> getALlCouse() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT Courses.id, Courses.code, Courses.title, Courses.credits, Courses.departmentCode "
                + "FROM Departments INNER JOIN "
                + "Courses ON Departments.Code = Courses.departmentCode";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int c_id = rs.getInt("id");
                String c_code = rs.getString("code");
                String c_title = rs.getString("title");
                int c_credits = rs.getInt("credits");

                Department department = depDAO.getDepartmentByCode(rs.getString("departmentCode"));

                Course c = new Course(c_id, c_code, c_title, c_credits, department);
                list.add(c);

            }
        } catch (Exception e) {
        }
        return list;
    }
//Ä‘Ã£ fix

    public void insertCourse(Course c) {
        String sql = "INSERT INTO Courses(code, title, credits, departmentCode) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, c.getCode());
            st.setString(2, c.getTitle());
            st.setInt(3, c.getCredits());
            st.setString(4, c.getDepartment().getCode());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fail" +e.getMessage());
            e.printStackTrace();
        }
    }
//Ä‘Ã£ fix

    public Course getCourseById(int id) {
        String sql = "SELECT * FROM Courses WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Department department = depDAO.getDepartmentByCode(rs.getString("departmentCode"));
                return new Course(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getInt("credits"),
                        department
                );
            }
        } catch (SQLException e) {
            System.out.println("Fail to get course by ID: " +e.getMessage());
        }
        return null;
    }
//Ä‘Ã£ fix

    public void deleteCourse(int id) {
        String sql = "DELETE FROM Courses WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// Ä‘Ã£ fix

    public void updateCourse(Course c) {
        String sql = "UPDATE Courses SET code=?, title=?, credits=?, departmentCode=? WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, c.getCode());
            st.setString(2, c.getTitle());
            st.setInt(3, c.getCredits());
            st.setString(4, c.getDepartment().getCode());
            st.setInt(5, c.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
//        List<Course> list = dao.getALlCouse();
//        for (Course s : list) {
//            System.out.println(s);
//        }
//
//        Course s = dao.getCourseById(1);
//        String code = "PRO123";
//        String title = "abc"; 
//        int credit = 3;
//        DepartmentDAO depDAO = new DepartmentDAO();
//        Department d = depDAO.getDepartmentByCode("ACC");
//        
//        Course aCourse = new Course(0, code, title, credit, d);
//        
//        dao.insertCourse(aCourse);

//        int id = 1;
//        dao.deleteCourse(id);
    }

}

