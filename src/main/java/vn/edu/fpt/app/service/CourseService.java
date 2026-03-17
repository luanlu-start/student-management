package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int id) {
        return courseRepository.findById(id).orElse(null);
    }

    public List<Course> getCoursesByDepartment(String departmentCode) {
        return courseRepository.findByDepartment_Code(departmentCode);
    }

    @Transactional
    public boolean insertCourse(Course course) {
        try {
            courseRepository.save(course);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to insert course: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateCourse(Course course) {
        if (!courseRepository.existsById(course.getId())) return false;
        try {
            courseRepository.save(course);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to update course: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteCourse(int id) {
        if (!courseRepository.existsById(id)) return false;
        try {
            courseRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete course: " + e.getMessage());
            return false;
        }
    }
}
