package vn.edu.fpt.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Assessment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "weight")
    private double weight;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


}
