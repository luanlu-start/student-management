package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {

    List<Assessment> findByCourse_IdOrderById(int courseId);
}


