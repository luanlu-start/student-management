package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean insertNewStudent(Student student) {
        try {
            studentRepository.save(student);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to insert new student: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) return false;
        try {
            studentRepository.save(student);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to update student: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteStudentById(int id) {
        if (!studentRepository.existsById(id)) return false;
        try {
            studentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete student: " + e.getMessage());
            return false;
        }
    }

    /** Lá»c theo tÃªn */
    public List<Student> filterByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    /** Lá»c theo mÃ£ khoa */
    public List<Student> filterByDepartmentCode(String departmentCode) {
        return studentRepository.findByDepartment_Code(departmentCode);
    }

    /** Lá»c theo cáº£ tÃªn vÃ  mÃ£ khoa */
    public List<Student> filterByBoth(String departmentCode, String name) {
        return studentRepository.findByDepartment_CodeAndNameContainingIgnoreCase(departmentCode, name);
    }
}


