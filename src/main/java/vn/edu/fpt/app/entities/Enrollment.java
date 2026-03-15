/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.entities;

import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Student;
import jakarta.persistence.*;

/**
 *
 * @author Legion
 */
@Entity
@Table(name = "Enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private vn.edu.fpt.app.entities.Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classId", nullable = false)
    private vn.edu.fpt.app.entities.Classes cls;

    public Enrollment() {
    }

    public Enrollment(int id, vn.edu.fpt.app.entities.Student student, vn.edu.fpt.app.entities.Classes cls) {
        this.id = id;
        this.student = student;
        this.cls = cls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public vn.edu.fpt.app.entities.Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public vn.edu.fpt.app.entities.Classes getCls() {
        return cls;
    }

    public void setCls(Classes cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "Enrollment{" + "id=" + id + ", student=" + student + ", cls=" + cls + '}';
    }

   
}

