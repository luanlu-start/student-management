package vn.edu.fpt.app.service;

import org.springframework.stereotype.Service;
import vn.edu.fpt.app.entities.Semester;

import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterService {

    public List<Semester> getAll() {
        return new ArrayList<>();
    }
}
