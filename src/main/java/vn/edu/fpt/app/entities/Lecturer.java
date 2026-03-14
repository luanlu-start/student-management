package vn.edu.fpt.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Lecturers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "departmentCode")
    private Department department;


}
