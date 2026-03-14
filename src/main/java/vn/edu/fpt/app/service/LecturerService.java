package vn.edu.fpt.app.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.repository.LecturerReposity;

import java.util.List;
import java.util.Optional;

@Service
public class LecturerService {
   private final LecturerReposity lecturerReposity;


    public LecturerService(LecturerReposity lecturerReposity) {
        this.lecturerReposity = lecturerReposity;
    }

    public List<Lecturer> getAll() {
        return lecturerReposity.findAll();
    }

    public Lecturer getById(int id) {
        return lecturerReposity.findById(id).orElse(null);
    }

    public void save (Lecturer lecturer) {
        lecturerReposity.save(lecturer);
    }

    public void delete(int id) {
        lecturerReposity.deleteById(id);
    }

    public Page<Lecturer> getAllLecturers(Pageable pageable) {
        return lecturerReposity.findAll(pageable);
    }

}
