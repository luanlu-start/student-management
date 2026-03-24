package vn.edu.fpt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.app.entities.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByDepartment_Code(String departmentCode);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, int id);
}

