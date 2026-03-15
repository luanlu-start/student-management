package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    // findAll(), findById(id), save(), deleteById(id) â€” provided by JpaRepository
}


