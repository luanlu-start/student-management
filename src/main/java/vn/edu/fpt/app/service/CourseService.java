package vn.edu.fpt.app.service;

import vn.edu.fpt.app.dto.CourseDTO;
import vn.edu.fpt.app.entities.Course;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.app.repository.DepartmentRepository;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public CourseService(CourseRepository courseRepository, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
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
    public void insertCourse(CourseDTO dto) {

        if (courseRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("CODE_EXISTS");
        }

        Course course = new Course();

        course.setCode(dto.getCode().trim());
        course.setTitle(dto.getTitle().trim());
        course.setCredits(dto.getCredits());

        Department dep = departmentRepository.findById(dto.getDepartmentCode())
                .orElseThrow(() -> new RuntimeException("DEPARTMENT_NOT_FOUND"));

        course.setDepartment(dep);

        courseRepository.save(course);
    }

    @Transactional
    public void updateCourse(CourseDTO dto) {

        Course course = courseRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("NOT_FOUND"));

        if (courseRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
            throw new RuntimeException("CODE_EXISTS");
        }

        course.setCode(dto.getCode().trim());
        course.setTitle(dto.getTitle().trim());
        course.setCredits(dto.getCredits());

        Department dep = departmentRepository.findById(dto.getDepartmentCode())
                .orElseThrow(() -> new RuntimeException("DEPARTMENT_NOT_FOUND"));

        course.setDepartment(dep);

        courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(int id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NOT_FOUND"));

        courseRepository.delete(course);
    }
}
