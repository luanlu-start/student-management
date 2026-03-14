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
    @Column(name = "Code", length = 10)
    private String code;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "DepartmentHead")
    private String departmentHead;


    @Column(name = "Phone")
    private String phone;

    @Column(name = "Email")
    private String email;


}
