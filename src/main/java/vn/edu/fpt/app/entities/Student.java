package vn.edu.fpt.app.entities;

import jakarta.persistence.*;
import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Entity
@Table(name = "Students", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date birthdate;
    private String gender;
    private String address;
    private String city;

    @ManyToOne
    @JoinColumn(name = "departmentCode", referencedColumnName = "code")
    private Department department;


    private String email;
    private String phone;


}
