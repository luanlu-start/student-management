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
@Table(name = "Lecturers")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "title", length = 50)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentCode", nullable = false)
    private vn.edu.fpt.app.entities.Department department;

    public Lecturer() {
    }

    public Lecturer(int id, String name, String email, String phone, String title, vn.edu.fpt.app.entities.Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.title = title;
        this.department = department;
    }

    public Lecturer(String name, String email, String phone, String title, vn.edu.fpt.app.entities.Department department) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.title = title;
        this.department = department;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public vn.edu.fpt.app.entities.Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Lecturer{" + "id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", title=" + title + ", department=" + department + '}';
    }
    
}

