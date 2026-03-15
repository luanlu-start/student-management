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
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    /**
     * Java field "password" maps to DB column "passwordHash".
     * Value is stored as MD5 hash.
     */
    @Column(name = "passwordHash", nullable = false, length = 255)
    private String password;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturerId")
    private Lecturer lecturer;

    public User() {
    }

    public User(String username, String password, String role, Lecturer lecturer) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.lecturer = lecturer;
    }

    public User(int id, String username, String password, String role, Lecturer lecturer) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.lecturer = lecturer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", lecturer=" + lecturer + '}';
    } 
    
}
