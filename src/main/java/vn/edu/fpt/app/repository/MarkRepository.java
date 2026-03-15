package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.app.entities.MarkId;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<vn.edu.fpt.app.entities.Mark, MarkId> {

    List<vn.edu.fpt.app.entities.Mark> findByEnrollment_Id(int enrollId);

    List<vn.edu.fpt.app.entities.Mark> findByAssessment_Id(int assessmentId);

    boolean existsByEnrollment_IdAndAssessment_Id(int enrollId, int assessmentId);

    /** Láº¥y táº¥t cáº£ Ä‘iá»ƒm cá»§a má»™t sinh viÃªn qua báº£ng Enrollment */
    @Query("SELECT m FROM Mark m WHERE m.enrollment.student.id = :studentId")
    List<vn.edu.fpt.app.entities.Mark> findByStudentId(@Param("studentId") int studentId);

    /** Láº¥y táº¥t cáº£ Ä‘iá»ƒm cá»§a cÃ¡c sinh viÃªn trong má»™t lá»›p */
    @Query("SELECT m FROM Mark m WHERE m.enrollment.cls.id = :classId")
    List<vn.edu.fpt.app.entities.Mark> findByClassId(@Param("classId") int classId);

    @Modifying
    @Transactional
    void deleteByEnrollment_Id(int enrollId);
}


