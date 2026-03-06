package vn.edu.fpt.app.service;

import org.springframework.stereotype.Service;
import vn.edu.fpt.app.entities.Course;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    public List<Course> getAll() {
        return new ArrayList<>();
    }
}
