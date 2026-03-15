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

/**
 *
 * @author Legion
 */
public class DepartmentDAO extends DBContext {

    public List<Department> getAllDepartments() {
        String sql = "SELECT * FROM Departments";
        List<Department> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString("Code");
                String name = rs.getString("Name");
                String departmentHead = rs.getString("DepartmentHead");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");

                Department department = new Department(code, name, departmentHead, phone, email);

                list.add(department);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all departments" + e.getMessage());
        }
        return list;
    }

    public Department getDepartmentByCode(String code) {
        String sql = "SELECT * FROM Departments WHERE code = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String departmentHead = rs.getString("DepartmentHead");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");

                Department department = new Department(code, name, departmentHead, phone, email);
                return department;
            }
        } catch (Exception e) {
            System.out.println("Error when finding department by code: " + e.getMessage());
        }
        return null;
    }

    public boolean updateDepartmentByCode(String name, String departmentHead, String email, String phone, String code) {
        String sql = "UPDATE [dbo].[Departments]"
                + "   SET [Name] = ?"
                + "      ,[DepartmentHead] = ?"
                + "      ,[Email] = ?"
                + "      ,[Phone] = ?"
                + " WHERE Code = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, departmentHead);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, code);

            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to update department: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteDepartmentByCode(String code) {
        String sql = "DELETE FROM Departments WHERE Code = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to delete department by ID: " + e.getMessage());
        }
        return false;
    }

    public boolean addNewDepartment(Department department) {
        String sql = "INSERT INTO [dbo].[Departments] "
                + "           ([Code],[Name],[DepartmentHead],[Email],[Phone]) "
                + "     VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, department.getCode());
            ps.setString(2, department.getName());
            ps.setString(3, department.getDepartmentHead());
            ps.setString(4, department.getEmail());
            ps.setString(5, department.getPhone());

            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to add new department! " + e.getMessage());
        }
        return false;

    }

    public static void main(String[] args) {
        DepartmentDAO dao = new DepartmentDAO();
//        List<Department> list = dao.getAllDepartments();
//        for (Department d : list) {
//            System.out.println(d);
//        }

//        dao.deleteDepartmentByCode("AI");
//
//        String name = "A E";
//        String depHead = "VÃµ Há»“ng Khanh";
//        String email = "khanhvh@university.edu.vn";
//        String phone = "02838445678";
//        String code = "AE";
//
//        Department dep = new Department(code, name, depHead, phone, email);
//        dao.addNewDepartment(dep);

    }

}

