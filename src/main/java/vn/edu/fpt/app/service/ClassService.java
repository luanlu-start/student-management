package vn.edu.fpt.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.app.dto.ClassDTO;
import vn.edu.fpt.app.entities.*;
import vn.edu.fpt.app.repository.*;

import java.util.List;

@Service
public class ClassService {

    private final ClassesRepository classesRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;
    private final SemesterRepository semesterRepository;

    public ClassService(ClassesRepository classesRepository, EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
                        LecturerRepository lecturerRepository, SemesterRepository semesterRepository) {
        this.classesRepository = classesRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.lecturerRepository = lecturerRepository;
        this.semesterRepository = semesterRepository;
    }

    /**
     * Lấy tất cả lớp học kèm số sinh viên (studentCount là @Transient)
     */
    public List<Classes> getAllClasses() {
        List<Classes> list = classesRepository.findAll();
        list.forEach(cls -> cls.setStudentCount((int) enrollmentRepository.countByCls_Id(cls.getId())));
        return list;
    }

    /**
     * Lấy lớp theo giảng viên để lecturer chỉ thấy lớp mình dạy.
     */
    public List<Classes> getClassesByLecturerId(int lecturerId) {
        List<Classes> list = classesRepository.findByLecturer_Id(lecturerId);
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
    public void insertClass(ClassDTO dto) {

        // Validate duplicate code (nếu cần)
        if (classesRepository.existsByCode(dto.getCode().trim())) {
            throw new RuntimeException("CLASS_CODE_EXISTS");
        }

        Classes classes = new Classes();

        // Map từ DTO → Entity
        classes.setCode(dto.getCode().trim());
        classes.setRoom(dto.getRoom().trim());
        classes.setSchedule(dto.getSchedule().trim());

        // Lấy entity liên quan
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("COURSE_NOT_FOUND"));

        Lecturer lecturer = lecturerRepository.findById(dto.getLecturerId())
                .orElseThrow(() -> new RuntimeException("LECTURER_NOT_FOUND"));

        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new RuntimeException("SEMESTER_NOT_FOUND"));

        // Set relationship
        classes.setCourse(course);
        classes.setLecturer(lecturer);
        classes.setSemester(semester);

        // Save
        classesRepository.save(classes);
    }

    @Transactional
    public void updateClass(int classId, ClassDTO dto) {

        // 1️⃣ Lấy lớp từ DB
        Classes cls = classesRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("CLASS_NOT_FOUND"));

        // 2️⃣ Lấy các entity liên quan
        Lecturer lecturer = lecturerRepository.findById(dto.getLecturerId())
                .orElseThrow(() -> new RuntimeException("LECTURER_NOT_FOUND"));

        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new RuntimeException("SEMESTER_NOT_FOUND"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("COURSE_NOT_FOUND"));

        // 3️⃣ Check duplicate class code (chỉ khi code thay đổi)
        String newCode = dto.getCode().trim();
        if (!cls.getCode().equals(newCode) && classesRepository.existsByCode(newCode)) {
            throw new RuntimeException("CLASS_CODE_EXISTS");
        }

        // 4️⃣ Map fields từ DTO → Entity
        cls.setCode(newCode);
        cls.setRoom(dto.getRoom().trim());
        cls.setSchedule(dto.getSchedule().trim());
        cls.setLecturer(lecturer);
        cls.setSemester(semester);
        cls.setCourse(course);

        // 5️⃣ Save
        classesRepository.save(cls);
    }

    @Transactional
    public void deleteClassById(int classId) {
        Classes cls = classesRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("CLASS_NOT_FOUND"));

        classesRepository.delete(cls);
    }
}
