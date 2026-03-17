package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Semester;
import vn.edu.fpt.app.repository.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    public Semester getSemesterById(int id) {
        return semesterRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean addNewSemester(Semester semester) {
        try {
            semesterRepository.save(semester);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to add new semester: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateSemester(Semester semester) {
        if (!semesterRepository.existsById(semester.getId())) return false;
        try {
            semesterRepository.save(semester);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to update semester: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteSemesterById(int id) {
        if (!semesterRepository.existsById(id)) return false;
        try {
            semesterRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete semester: " + e.getMessage());
            return false;
        }
    }
}
