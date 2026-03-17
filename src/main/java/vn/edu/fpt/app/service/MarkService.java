package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.Mark;
import vn.edu.fpt.app.entities.MarkId;
import vn.edu.fpt.app.repository.MarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarkService {

    private final MarkRepository markRepository;
    private final EnrollmentService enrollmentService;
    private final AssessmentService assessmentService;

    public MarkService(MarkRepository markRepository, EnrollmentService enrollmentService, AssessmentService assessmentService) {
        this.markRepository = markRepository;
        this.enrollmentService = enrollmentService;
        this.assessmentService = assessmentService;
    }

    public List<Mark> getAllMarks() {
        return markRepository.findAll();
    }

    public Mark getMarkById(int enrollId, int assessmentId) {
        return markRepository.findById(new MarkId(enrollId, assessmentId)).orElse(null);
    }

    public List<Mark> getMarksByEnrollmentId(int enrollId) {
        return markRepository.findByEnrollment_Id(enrollId);
    }

    public List<Mark> getMarksByStudentId(int studentId) {
        return markRepository.findByStudentId(studentId);
    }

    public List<Mark> getMarksByAssessmentId(int assessmentId) {
        return markRepository.findByAssessment_Id(assessmentId);
    }

    public boolean markExists(int enrollId, int assessmentId) {
        return markRepository.existsByEnrollment_IdAndAssessment_Id(enrollId, assessmentId);
    }

    @Transactional
    public boolean createMark(int enrollId, int assessmentId, double markValue) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollId);
        Assessment assessment = assessmentService.getAssessmentById(assessmentId);
        if (enrollment == null || assessment == null) return false;
        try {
            markRepository.save(new Mark(enrollment, assessment, markValue));
            return true;
        } catch (Exception e) {
            System.out.println("Failed to create mark: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateMark(int enrollId, int assessmentId, double markValue) {
        Mark mark = markRepository.findById(new MarkId(enrollId, assessmentId)).orElse(null);
        if (mark == null) return false;
        mark.setMark(markValue);
        try {
            markRepository.save(mark);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update mark: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteMark(int enrollId, int assessmentId) {
        MarkId id = new MarkId(enrollId, assessmentId);
        if (!markRepository.existsById(id)) return false;
        try {
            markRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to delete mark: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteMarksByEnrollmentId(int enrollId) {
        try {
            markRepository.deleteByEnrollment_Id(enrollId);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to delete marks by enrollment ID: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns Map<"enrollId_assessmentId", Mark> for one class.
     */
    public Map<String, Mark> getMarksMapForClass(int classId) {
        Map<String, Mark> marksMap = new HashMap<>();
        markRepository.findByClassId(classId).forEach(m -> {
            int enrollId = m.getEnrollment().getId();
            int assessmentId = m.getAssessment().getId();
            marksMap.put(enrollId + "_" + assessmentId, m);
        });
        return marksMap;
    }

    /**
     * Returns Map<studentId, Map<assessmentId, Mark>> for one class.
     */
    public Map<Integer, Map<Integer, Mark>> getMarksByStudentAndAssessmentForClass(int classId) {
        Map<Integer, Map<Integer, Mark>> marksByStudentAssessment = new HashMap<>();
        markRepository.findByClassId(classId).forEach(mark -> {
            int studentId = mark.getEnrollment().getStudent().getId();
            int assessmentId = mark.getAssessment().getId();
            marksByStudentAssessment
                    .computeIfAbsent(studentId, ignored -> new HashMap<>())
                    .put(assessmentId, mark);
        });
        return marksByStudentAssessment;
    }
}
