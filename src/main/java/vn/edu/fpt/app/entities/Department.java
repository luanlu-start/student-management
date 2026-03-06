package vn.edu.fpt.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Departments", schema = "dbo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "DepartmentHead")
    private String departmentHead;


    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;


}
