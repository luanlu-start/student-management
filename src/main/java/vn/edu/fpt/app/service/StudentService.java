package vn.edu.fpt.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.fpt.app.entities.Student;
import vn.edu.fpt.app.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    public List<Student> getAll() {
        return repo.findAll();
    }

    public Student getById(int id) {
        Optional<Student> opt = repo.findById(id);
        return opt.orElse(null);
    }

    public void save(Student s) {
        repo.save(s);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }
}
