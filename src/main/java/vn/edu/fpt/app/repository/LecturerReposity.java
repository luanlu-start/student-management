package vn.edu.fpt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.app.entities.Lecturer;

public interface LecturerReposity extends JpaRepository<Lecturer, Integer> {
}
