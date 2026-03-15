package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByDepartment_Code(String departmentCode);

    List<Student> findByDepartment_CodeAndNameContainingIgnoreCase(String departmentCode, String name);
}


