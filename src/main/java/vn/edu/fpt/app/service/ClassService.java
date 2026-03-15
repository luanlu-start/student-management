package vn.edu.fpt.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.app.entities.*;
import vn.edu.fpt.app.repository.ClassesRepository;
import vn.edu.fpt.app.repository.EnrollmentRepository;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private SemesterService semesterService;

    /** Lấy tất cả lớp học kèm số sinh viên (studentCount là @Transient) */
    public List<Classes> getAllClasses() {
        List<Classes> list = classesRepository.findAll();
        list.forEach(cls -> cls.setStudentCount((int) enrollmentRepository.countByCls_Id(cls.getId())));
        return list;
    }

    public Classes getClassById(int id) {
        Classes cls = classesRepository.findById(id).orElse(null);
        if (cls != null) {
            cls.setStudentCount((int) enrollmentRepository.countByCls_Id(cls.getId()));
        }
        return cls;
    }

    public List<Student> getStudentsByClassId(int classId) {
        return classesRepository.findStudentsByClassId(classId);
    }

    @Transactional
    public boolean addNewClass(String code, int courseId, int lecturerId, int semesterId, String room, String schedule) {
        Course course = courseService.getCourseById(courseId);
        Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
        Semester semester = semesterService.getSemesterById(semesterId);
        if (course == null || semester == null) return false;
        try {
            classesRepository.save(new Classes(0, code, room, schedule, course, lecturer, semester, 0));
            return true;
        } catch (Exception e) {
            System.out.println("Fail to add new class: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateClass(int classId, int lecturerId, int semesterId, String room, String schedule) {
        Classes cls = classesRepository.findById(classId).orElse(null);
        if (cls == null) return false;
        Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
        Semester semester = semesterService.getSemesterById(semesterId);
        cls.setLecturer(lecturer);
        cls.setSemester(semester);
        cls.setRoom(room);
        cls.setSchedule(schedule);
        try {
            classesRepository.save(cls);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to update class: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteClassById(int id) {
        if (!classesRepository.existsById(id)) return false;
        try {
            classesRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete class: " + e.getMessage());
            return false;
        }
    }
}

