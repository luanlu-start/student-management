/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.entities;

import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.entities.Semester;
import jakarta.persistence.*;

/**
 *
 * @author Legion
 */
@Entity
@Table(name = "Classes")
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "room", length = 50)
    private String room;

    @Column(name = "schedule", length = 100)
    private String schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private vn.edu.fpt.app.entities.Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturerId")
    private vn.edu.fpt.app.entities.Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semesterId", nullable = false)
    private vn.edu.fpt.app.entities.Semester semester;

    @Transient
    private int studentCount;

    public Classes() {
    }

    public Classes(int id, String code, String room, String schedule, vn.edu.fpt.app.entities.Course course, vn.edu.fpt.app.entities.Lecturer lecturer, vn.edu.fpt.app.entities.Semester semester, int studentCount) {
        this.id = id;
        this.code = code;
        this.room = room;
        this.schedule = schedule;
        this.course = course;
        this.lecturer = lecturer;
        this.semester = semester;
        this.studentCount = studentCount;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public vn.edu.fpt.app.entities.Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public vn.edu.fpt.app.entities.Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public vn.edu.fpt.app.entities.Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    @Override
    public String toString() {
        return "Classes{" + "id=" + id + ", code=" + code + ", room=" + room + ", schedule=" + schedule + ", course=" + course + ", lecturer=" + lecturer + ", semester=" + semester + ", studentCount=" + studentCount + '}';
    }

}

