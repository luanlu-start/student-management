package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findByCls_Id(int classId);

    Optional<Enrollment> findByStudent_IdAndCls_Id(int studentId, int classId);

    long countByCls_Id(int classId);

    boolean existsByStudent_IdAndCls_Id(int studentId, int classId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Enrollment e WHERE e.student.id = :studentId AND e.cls.id = :classId")
    void deleteByStudentIdAndClassId(@Param("studentId") int studentId, @Param("classId") int classId);
}


