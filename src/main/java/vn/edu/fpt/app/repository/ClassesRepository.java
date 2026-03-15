package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Integer> {

    /** Láº¥y danh sÃ¡ch sinh viÃªn Ä‘ang há»c trong má»™t lá»›p */
    @Query("SELECT e.student FROM Enrollment e WHERE e.cls.id = :classId")
    List<Student> findStudentsByClassId(@Param("classId") int classId);
}


