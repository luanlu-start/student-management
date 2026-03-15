package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.repository.AssessmentRepository;
import vn.edu.fpt.app.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CourseService courseService;

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Assessment getAssessmentById(int id) {
        return assessmentRepository.findById(id).orElse(null);
    }

    public List<Assessment> getAssessmentsByCourseId(int courseId) {
        return assessmentRepository.findByCourse_IdOrderById(courseId);
    }

    @Transactional
    public boolean createAssessment(String type, double weight, int courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) return false;
        try {
            assessmentRepository.save(new Assessment(0, type, weight, course));
            return true;
        } catch (Exception e) {
            System.out.println("Fail to create assessment: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateAssessment(int id, String type, double weight, int courseId) {
        Assessment assessment = assessmentRepository.findById(id).orElse(null);
        if (assessment == null) return false;
        Course course = courseService.getCourseById(courseId);
        if (course == null) return false;
        assessment.setType(type);
        assessment.setWeight(weight);
        assessment.setCourse(course);
        try {
            assessmentRepository.save(assessment);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to update assessment: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteAssessmentById(int id) {
        if (!assessmentRepository.existsById(id)) return false;
        try {
            assessmentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete assessment: " + e.getMessage());
            return false;
        }
    }
}


