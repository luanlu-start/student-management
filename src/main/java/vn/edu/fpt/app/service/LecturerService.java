package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Lecturer;
import vn.edu.fpt.app.repository.LecturerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LecturerService {

    private final LecturerRepository lecturerRepository;

    public LecturerService(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }

    public Lecturer getLecturerById(int id) {
        return lecturerRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean addNewLecturer(Lecturer lecturer) {
        try {
            lecturerRepository.save(lecturer);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to add new lecturer: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateLecturer(Lecturer lecturer) {
        if (!lecturerRepository.existsById(lecturer.getId())) return false;
        try {
            lecturerRepository.save(lecturer);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to update lecturer: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteLecturerById(int id) {
        if (!lecturerRepository.existsById(id)) return false;
        try {
            lecturerRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete lecturer: " + e.getMessage());
            return false;
        }
    }

    /** Lá»c theo tÃªn */
    public List<Lecturer> filterByName(String name) {
        return lecturerRepository.findByNameContainingIgnoreCase(name);
    }

    /** Lá»c theo mÃ£ khoa */
    public List<Lecturer> filterByDepartmentCode(String departmentCode) {
        return lecturerRepository.findByDepartment_Code(departmentCode);
    }

    /** Lá»c theo cáº£ tÃªn vÃ  mÃ£ khoa */
    public List<Lecturer> filterByBoth(String departmentCode, String name) {
        return lecturerRepository.findByDepartment_CodeAndNameContainingIgnoreCase(departmentCode, name);
    }
}
