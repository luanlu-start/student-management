/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Lecturer;

/**
 *
 * @author Legion
 */
public class LecturerDAO extends DBContext {

    public List<Lecturer> getAllLecturer() {
        List<Lecturer> list = new ArrayList<>();
        String sql = "SELECT L.id, L.name, L.email, L.departmentCode, L.phone, L.title, "
                + "D.Name AS department_name "
                + "FROM Lecturers L "
                + "LEFT JOIN Departments D ON L.departmentCode = D.Code";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String title = rs.getString("title");

                Department department = new Department();
                department.setCode(rs.getString("departmentCode"));
                department.setName(rs.getString("department_name"));

                Lecturer lecturer = new Lecturer(id, name, email, phone, title, department);
                list.add(lecturer);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all lecturer: " + e.getMessage());
        }
        return list;
    }

    public Lecturer getLecturerById(int id) {
        String sql = "SELECT L.id, L.name, L.email, L.departmentCode, L.phone, L.title, "
                + "D.Name AS department_name "
                + "FROM Lecturers L "
                + "LEFT JOIN Departments D ON L.departmentCode = D.Code "
                + "WHERE L.id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String title = rs.getString("title");

                Department department = new Department();
                department.setCode(rs.getString("departmentCode"));
                department.setName(rs.getString("department_name"));

                Lecturer lecturer = new Lecturer(id, name, email, phone, title, department);
                return lecturer;
            }
        } catch (Exception e) {
            System.out.println("Fail to get lecturer by ID: " + e.getMessage());
        }
        return null;
    }

    public Boolean updateLecturerById(Lecturer lecturer) {
        String sql = "UPDATE [dbo].[Lecturers] "
                + "SET [name] = ? "
                + "      ,[email] = ? "
                + "      ,[departmentCode] = ? "
                + "      ,[phone] = ? "
                + "      ,[title] = ? "
                + " WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lecturer.getName());
            ps.setString(2, lecturer.getEmail());
            ps.setString(3, lecturer.getDepartment().getCode());
            ps.setString(4, lecturer.getPhone());
            ps.setString(5, lecturer.getTitle());
            ps.setInt(6, lecturer.getId());
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to update lecturer: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteLecturerById(int id) {
        String sql = "DELETE FROM Lecturers WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to delete lecturer! " + e.getMessage());
        }
        return false;
    }

    public boolean addNewLecturer(Lecturer lecturer) {
        String sql = "INSERT INTO [dbo].[Lecturers] "
                + "           ([name] "
                + "           ,[email] "
                + "           ,[departmentCode] "
                + "           ,[phone] "
                + "           ,[title]) "
                + "     VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lecturer.getName());
            ps.setString(2, lecturer.getEmail());
            ps.setString(3, lecturer.getDepartment().getCode());
            ps.setString(4, lecturer.getPhone());
            ps.setString(5, lecturer.getTitle());

            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to add new lecturer: " + e.getMessage());
        }
        return false;
    }

    public List<Lecturer> FillterByname(String nameKeyword) {
        List<Lecturer> list = new ArrayList<>();
        String sql = "SELECT L.*, D.Name AS department_name "
                + "FROM Lecturers L LEFT JOIN Departments D ON L.departmentCode = D.Code "
                + "WHERE LOWER(L.name) LIKE LOWER(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nameKeyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String title = rs.getString("title");

                    Department department = new Department();
                    department.setCode(rs.getString("departmentCode"));
                    department.setName(rs.getString("department_name"));

                    Lecturer lecturer = new Lecturer(id, name, email, phone, title, department);
                    list.add(lecturer);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to filter lecturer by name: " + e.getMessage());
        }
        return list;
    }

    public List<Lecturer> FillterByDepartmetCode(String depCode) {
        List<Lecturer> list = new ArrayList<>();
        String sql = "SELECT L.*, D.Name AS department_name "
                + "FROM Lecturers L LEFT JOIN Departments D ON L.departmentCode = D.Code "
                + "WHERE L.departmentCode = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, depCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String title = rs.getString("title");

                    Department department = new Department();
                    department.setCode(rs.getString("departmentCode"));
                    department.setName(rs.getString("department_name"));

                    Lecturer lecturer = new Lecturer(id, name, email, phone, title, department);
                    list.add(lecturer);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to filter lecturer by code: " + e.getMessage());
        }
        return list;
    }

    public List<Lecturer> FillterBoth(String depCode, String nameKeyword) {
        List<Lecturer> list = new ArrayList<>();
        String sql = "SELECT L.*, D.Name AS department_name "
                + "FROM Lecturers L LEFT JOIN Departments D ON L.departmentCode = D.Code "
                + "WHERE L.departmentCode = ? AND LOWER(L.name) LIKE LOWER(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, depCode);
            ps.setString(2, "%" + nameKeyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String title = rs.getString("title");

                    Department department = new Department();
                    department.setCode(rs.getString("departmentCode"));
                    department.setName(rs.getString("department_name"));

                    Lecturer lecturer = new Lecturer(id, name, email, phone, title, department);
                    list.add(lecturer);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to filter lecturer by both: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        LecturerDAO dao = new LecturerDAO();
//        List<Lecturer> list = dao.getAllLecturer();
//        for (Lecturer lecturer : list) {
//            System.out.println(lecturer);
//        }

//        String name = "Nguyá»…n HoÃ ng HoÃ ng";
//        String email = "hoangntt@fpt.edu.vn";
//        String depCode = "SE";
//
//        DepartmentDAO depdao = new DepartmentDAO();
//        Department dep = depdao.getDepartmentByCode(depCode);
//        String phone = "0901234565";
//        String title = "Ths";
//
//        Lecturer lecturer = new Lecturer(1, name, email, phone, title, dep);
//        dao.addNewLecturer(lecturer);
//
//        Lecturer l = dao.getLecturerById(4);
//        System.out.println(l);
    }
}

