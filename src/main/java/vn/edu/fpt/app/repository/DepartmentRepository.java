package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    // check trùng name khi add
    boolean existsByNameIgnoreCase(String name);

    // check trùng name khi update (trừ chính nó)
    boolean existsByNameIgnoreCaseAndCodeNot(String name, String code);

    // check trùng code (thường không cần vì @Id)
    boolean existsByCode(String code);
}
