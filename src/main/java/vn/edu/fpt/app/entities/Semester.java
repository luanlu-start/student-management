/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.entities;

import jakarta.persistence.*;
import java.sql.Date;

/**
 *
 * @author Legion
 */
@Entity
@Table(name = "Semesters")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "beginDate", nullable = false)
    private Date beginDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    public Semester() {
    }

    public Semester(int id, String code, int year, Date beginDate, Date endDate) {
        this.id = id;
        this.code = code;
        this.year = year;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public Semester(String code, int year, Date beginDate, Date endDate) {
        this.code = code;
        this.year = year;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    

    @Override
    public String toString() {
        return "Semester{" + "id=" + id + ", code=" + code + ", year=" + year + ", beginDate=" + beginDate + ", endDate=" + endDate + '}';
    }
    
    
}
