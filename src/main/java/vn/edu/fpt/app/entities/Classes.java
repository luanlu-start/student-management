package vn.edu.fpt.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "room")
    private String room;

    @Column(name = "schedule")
    private String schedule;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "student_count")
    private int studentCount;


}
