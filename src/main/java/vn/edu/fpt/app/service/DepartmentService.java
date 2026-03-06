package vn.edu.fpt.app.service;

import org.springframework.stereotype.Service;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.repository.DepartmentReposity;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentReposity departmentReposity;

    public DepartmentService(DepartmentReposity departmentReposity) {
        this.departmentReposity = departmentReposity;
    }

    public List<Department> getAll() {
        return departmentReposity.findAll();
    }




}
