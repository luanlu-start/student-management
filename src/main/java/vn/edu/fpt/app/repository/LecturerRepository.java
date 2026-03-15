package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {

    List<Lecturer> findByNameContainingIgnoreCase(String name);

    List<Lecturer> findByDepartment_Code(String departmentCode);

    List<Lecturer> findByDepartment_CodeAndNameContainingIgnoreCase(String departmentCode, String name);
}


