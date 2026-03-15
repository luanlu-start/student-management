/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.entities;

import jakarta.persistence.*;

/**
 *
 * @author Legion
 */
@Entity
@Table(name = "Departments")
public class Department {

    @Id
    @Column(name = "Code", nullable = false, length = 50)
    private String code;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "DepartmentHead", length = 100)
    private String departmentHead;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "Email", length = 100)
    private String email;

    public Department() {
    }

    public Department(String code, String name, String departmentHead, String phone, String email) {
        this.code = code;
        this.name = name;
        this.departmentHead = departmentHead;
        this.phone = phone;
        this.email = email;
    }
   

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Department{" + "code=" + code + ", name=" + name + ", departmentHead=" + departmentHead + ", phone=" + phone + ", email=" + email + '}';
    }   
}
