/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.entities;

import vn.edu.fpt.app.entities.Department;
import jakarta.persistence.*;

/**
 *
 * @author Legion
 */
@Entity
@Table(name = "Courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "credits", nullable = false)
    private int credits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentCode", nullable = false)
    private vn.edu.fpt.app.entities.Department department;

    public Course() {
    }

    public Course(int id, String code, String title, int credits, vn.edu.fpt.app.entities.Department department) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.department = department;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public vn.edu.fpt.app.entities.Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", code=" + code + ", title=" + title + ", credits=" + credits + ", department=" + department + '}';
    }

   
    
}

