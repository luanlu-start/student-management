package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.Mark;
import vn.edu.fpt.app.entities.MarkId;
import vn.edu.fpt.app.repository.MarkRepository;
import vn.edu.fpt.app.service.AssessmentService;
import vn.edu.fpt.app.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarkService {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AssessmentService assessmentService;

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
     * Tráº£ vá» Map<"studentId_assessmentId", Mark> cho má»™t lá»›p há»c.
     * DÃ¹ng Ä‘á»ƒ hiá»ƒn thá»‹ báº£ng Ä‘iá»ƒm nhanh trÃªn UI.
     */
    public Map<String, Mark> getMarksMapForClass(int classId) {
        Map<String, Mark> marksMap = new HashMap<>();
        markRepository.findByClassId(classId).forEach(m -> {
            int studentId = m.getEnrollment().getStudent().getId();
            int assessmentId = m.getAssessment().getId();
            marksMap.put(studentId + "_" + assessmentId, m);
        });
        return marksMap;
    }
}


