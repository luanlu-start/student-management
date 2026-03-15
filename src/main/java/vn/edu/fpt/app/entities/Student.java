/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.entities;

import vn.edu.fpt.app.entities.Department;
import jakarta.persistence.*;
import java.sql.Date;

/**
 *
 * @author Legion
 */
@Entity
@Table(name = "Students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentCode")
    private vn.edu.fpt.app.entities.Department department;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Phone", length = 20)
    private String phone;

    public Student() {
    }

    public Student(int id, String name, Date birthdate, String gender, String address, String city, vn.edu.fpt.app.entities.Department department, String email, String phone) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }

    public Student(String name, Date birthdate, String gender, String address, String city, vn.edu.fpt.app.entities.Department department, String email, String phone) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.department = department;
        this.email = email;
        this.phone = phone;
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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public vn.edu.fpt.app.entities.Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", birthdate=" + birthdate + ", gender=" + gender + ", address=" + address + ", city=" + city + ", department=" + department + ", email=" + email + ", phone=" + phone + '}';
    }

   
}

