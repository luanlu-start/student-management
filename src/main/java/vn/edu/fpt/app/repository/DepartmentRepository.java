package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    // findAll(), findById(code), save(), deleteById(code) â€” provided by JpaRepository
}


