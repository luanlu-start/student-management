package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Classes;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.repository.EnrollmentRepository;
import vn.edu.fpt.app.service.ClassService;
import vn.edu.fpt.app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassService classService;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(int id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean createEnrollment(int studentId, int classId) {
        Student student = studentService.getStudentById(studentId);
        Classes cls = classService.getClassById(classId);
        if (student == null || cls == null) return false;
        try {
            enrollmentRepository.save(new Enrollment(0, student, cls));
            return true;
        } catch (Exception e) {
            System.out.println("Fail to create enrollment: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteEnrollment(int studentId, int classId) {
        if (!enrollmentRepository.existsByStudent_IdAndCls_Id(studentId, classId)) return false;
        try {
            enrollmentRepository.deleteByStudentIdAndClassId(studentId, classId);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete enrollment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tráº£ vá» Map<studentId, enrollmentId> cho má»™t lá»›p há»c.
     * DÃ¹ng Ä‘á»ƒ tra nhanh enrollId khi nháº­p Ä‘iá»ƒm.
     */
    public Map<Integer, Integer> getStudentEnrollmentMap(int classId) {
        Map<Integer, Integer> map = new HashMap<>();
        enrollmentRepository.findByCls_Id(classId)
                .forEach(e -> map.put(e.getStudent().getId(), e.getId()));
        return map;
    }
}


