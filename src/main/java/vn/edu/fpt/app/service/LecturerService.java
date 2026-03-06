package vn.edu.fpt.app.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.repository.LecturerReposity;

import java.util.List;
import java.util.Optional;

@Service
public class LecturerService {
    @Autowired
    private LecturerReposity lecturerReposity;

    public LecturerService(LecturerReposity lecturerReposity) {
        this.lecturerReposity = lecturerReposity;
    }

    public List<Lecturer> findAll() {
        return lecturerReposity.findAll();
    }


    public Lecturer getById(int id) {
        Optional<Lecturer> opt = lecturerReposity.findById(id);

        return opt.orElse(null);
    }

    public Lecturer findLecturer(int id) {
        Optional<Lecturer> opt = lecturerReposity.findById(id);
        return opt.orElse(null);
    }

    public Lecturer createLecturer(Lecturer lecturer) {
        return lecturerReposity.save(lecturer);
    }


    @Transactional
    public Lecturer updateLecturer(int id, Lecturer newLecturer) {
        Optional<Lecturer> opt = lecturerReposity.findById(id);
        if (opt.isEmpty()) {
            return null;
        }


        Lecturer oldLecturer = opt.get();
        oldLecturer.setTitle(newLecturer.getTitle());
        oldLecturer.setName(newLecturer.getName());
        oldLecturer.setEmail(newLecturer.getEmail());
        oldLecturer.setPhone(newLecturer.getPhone());
        oldLecturer.setDepartment(newLecturer.getDepartment());

        return lecturerReposity.save(oldLecturer);


    }

    @Transactional
    public boolean deleteLecturer(int id) {
        if (!lecturerReposity.existsById(id)) {
            return false;
        }
        lecturerReposity.deleteById(id);
        return true;
    }

}
