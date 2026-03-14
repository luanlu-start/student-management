package vn.edu.fpt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.entities.Lecturer;

import java.util.List;

public interface LecturerReposity extends JpaRepository<Lecturer, Integer> {
    List<Lecturer> findByNameContainingIgnoreCase(String name);
    List<Lecturer> findByDepartment(Department department);
    List<Lecturer> findByDepartmentCodeAndNameContainingIgnoreCase(String depCode, String name);

}
