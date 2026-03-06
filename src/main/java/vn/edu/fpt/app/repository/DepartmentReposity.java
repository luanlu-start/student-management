package vn.edu.fpt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.app.entities.Department;

public interface DepartmentReposity extends JpaRepository<Department, String> {
}
