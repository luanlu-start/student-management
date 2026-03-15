/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.app.dao.DBContext;
import vn.edu.fpt.app.entities.Semester;

/**
 *
 * @author Legion
 */
public class SemesterDAO extends DBContext {

    public List<Semester> getAllSemester() {
        List<Semester> list = new ArrayList<>();
        String sql = "SELECT * FROM Semesters";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                int year = rs.getInt("year");
                Date beginDate = rs.getDate("beginDate");
                Date endDate = rs.getDate("endDate");
                Semester semester = new Semester(id, code, year, beginDate, endDate);
                list.add(semester);

            }
        } catch (SQLException e) {
            System.out.println("Fail to get all semester!" + e.getMessage());
        }
        return list;
    }

    public Semester getSemesterById(int id) {
        String sql = "SELECT * FROM Semesters WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String code = rs.getString("code");
                int year = rs.getInt("year");
                Date beginDate = rs.getDate("beginDate");
                Date endDate = rs.getDate("endDate");
                Semester semester = new Semester(id, code, year, beginDate, endDate);
                return semester;
            }
        } catch (Exception e) {
            System.out.println("Fail to get semester by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateSemestertByID(Semester semester) {
        String sql = "UPDATE [dbo].[Semesters] "
                + "   SET [code] = ? "
                + "      ,[year] = ? "
                + "      ,[beginDate] = ? "
                + "      ,[endDate] = ? "
                + " WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, semester.getCode());
            ps.setInt(2, semester.getYear());

            ps.setDate(3, semester.getBeginDate());
            ps.setDate(4, semester.getEndDate());
            ps.setInt(5, semester.getId());

            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to update senester: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteSemesterByCode(int id) {
        String sql = "DELETE FROM Semesters WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to delete semester by ID: " + e.getMessage());
        }
        return false;
    }

    public boolean addNewSemester(Semester semester) {
        String sql = "INSERT INTO [dbo].[Semesters] "
                + " ([code],[year],[beginDate],[endDate]) "
                + "     VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, semester.getCode());
            ps.setInt(2, semester.getYear());
            ps.setDate(3, semester.getBeginDate());
            ps.setDate(4, semester.getEndDate());
            
            int row = ps.executeUpdate();
            if(row > 0){
            return true;
            }

        } catch (Exception e) {
            System.out.println("Fail to add new semester: " +e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        SemesterDAO dao = new SemesterDAO();
//        List<Semester> list = dao.getAllSemester();
//        for (Semester semester : list) {
//            System.out.println(semester);
//        }
//        Semester semester = dao.getSemesterById(1);
//        System.out.println(semester);

//        String code = "FA27";
//        int year = 2027;
//        Date begin = Date.valueOf("2027-09-02");
//        Date end = Date.valueOf("2029-09-02");
//
//        Semester sem = new Semester(0, code, year, begin, end);
//        dao.addNewSemester(sem);
        

    }
}

