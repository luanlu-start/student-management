/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.dao;

import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.User;

/**
 *
 * @author Legion
 */
public class UserDAO extends DBContext {

    public String hashMD5(String str) {
        try {
            MessageDigest mes = MessageDigest.getInstance("MD5");
            byte[] messMD5 = mes.digest(str.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : messMD5) {
                String c = String.format("%02x", b);
                result.append(c);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.passwordHash, u.role, u.lecturerId, "
                + "  l.name AS lecturer_name, l.email AS lecturer_email, l.phone, l.title, "
                + "  d.Code AS department_code, d.Name AS department_name "
                + "FROM Users u "
                + "LEFT JOIN Lecturers l ON u.lecturerId = l.id "
                + "LEFT JOIN Departments d ON l.departmentCode = d.Code";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String passwordHash = rs.getString("passwordHash");
                String role = rs.getString("role");
                int lecturerId = rs.getInt("lecturerId");

                Lecturer lecturer = null;
                if (lecturerId > 0) {
                    lecturer = new Lecturer();
                    lecturer.setId(lecturerId);
                    lecturer.setName(rs.getString("lecturer_name"));
                    lecturer.setEmail(rs.getString("lecturer_email"));
                    lecturer.setPhone(rs.getString("phone"));
                    lecturer.setTitle(rs.getString("title"));

                    Department dept = new Department();
                    dept.setCode(rs.getString("department_code"));
                    dept.setName(rs.getString("department_name"));
                    lecturer.setDepartment(dept);
                }

                User user = new User(id, username, passwordHash, role, lecturer);
                list.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Fail to get the user list: " + e.getMessage());
        }
        return list;
    }

    public User checkLogin(String username, String password) {
        String sql = "SELECT "
                + "  u.id, u.username, u.passwordHash, u.role, u.lecturerId, "
                + "  l.name AS lecturer_name, l.email AS lecturer_email, l.phone, l.title, "
                + "  d.Code AS department_code, d.Name AS department_name "
                + "FROM "
                + "  Users u "
                + "LEFT JOIN Lecturers l ON u.lecturerId = l.id "
                + "LEFT JOIN Departments d ON l.departmentCode = d.Code "
                + "WHERE u.username = ? AND u.passwordHash = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            String hashPassword = hashMD5(password);
            ps.setString(2, hashPassword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String role = rs.getString("role");
                    int lecturerId = rs.getInt("lecturerId");

                    Lecturer lecturer = null;
                    if (lecturerId > 0) {
                        lecturer = new Lecturer();
                        lecturer.setId(lecturerId);
                        lecturer.setName(rs.getString("lecturer_name"));
                        lecturer.setEmail(rs.getString("lecturer_email"));
                        lecturer.setPhone(rs.getString("phone"));
                        lecturer.setTitle(rs.getString("title"));

                        Department dept = new Department();
                        dept.setCode(rs.getString("department_code"));
                        dept.setName(rs.getString("department_name"));
                        lecturer.setDepartment(dept);
                    }

                    User user = new User(id, username, hashPassword, role, lecturer);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to checkLogin: " + e.getMessage());
        }
        return null;
    }

    public boolean addNewUser(User user) {
        String sql = "INSERT INTO [dbo].[Users] ([username], [passwordHash], [role], [lecturerId]) "
                + "VALUES (?, ?, ?, ?)";

        String hashedPassword = hashMD5(user.getPassword());

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.getRole());

            if (user.getRole().equals("teacher") && user.getLecturer() != null && user.getLecturer().getId() > 0) {
                ps.setInt(4, user.getLecturer().getId());
            } else {
                // If new user is 'admin' or not conhensive, set NULL
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            int row = ps.executeUpdate();
            if (row > 0) {
                System.out.println("Add new user successfully!");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to add new user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public User getUserById(int id) {
        String sql = "SELECT "
                + "  u.id, u.username, u.passwordHash, u.role, u.lecturerId, "
                + "  l.name AS lecturer_name, l.email AS lecturer_email, l.phone, l.title, "
                + "  d.Code AS department_code, d.Name AS department_name "
                + "FROM "
                + "  Users u "
                + "LEFT JOIN Lecturers l ON u.lecturerId = l.id "
                + "LEFT JOIN Departments d ON l.departmentCode = d.Code "
                + "WHERE u.id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String passwordHash = rs.getString("passwordHash");
                    String role = rs.getString("role");
                    int lecturerId = rs.getInt("lecturerId");

                    Lecturer lecturer = null;
                    if (lecturerId > 0) {
                        lecturer = new Lecturer();
                        lecturer.setId(lecturerId);
                        lecturer.setName(rs.getString("lecturer_name"));
                        lecturer.setEmail(rs.getString("lecturer_email"));
                        lecturer.setPhone(rs.getString("phone"));
                        lecturer.setTitle(rs.getString("title"));

                        Department dept = new Department();
                        dept.setCode(rs.getString("department_code"));
                        dept.setName(rs.getString("department_name"));
                        lecturer.setDepartment(dept);
                    }

                    User user = new User(id, username, passwordHash, role, lecturer);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get user by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteUserById(int id) {
        String sql = "DELETE FROM Users WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Fail to delete user " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        List<User> list = dao.getAllUser();
        for (User u : list) {
            System.out.println(u);
        }

//        User user = dao.checkLogin("luke", "110606");
//        System.out.println(user);
//        String username = "Luan";
//        String pass = "28062003";
//        System.out.println(dao.hashMD5(pass));
    }
}

