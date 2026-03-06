package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
