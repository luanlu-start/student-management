package vn.edu.fpt.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "Semester")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "year")
    private int year;

    @Column(name = "begin_date")
    private Date beginDate;

    @Column(name = "end_date")
    private Date endDate;


}
