package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<vn.edu.fpt.app.entities.Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentByCode(String code) {
        return departmentRepository.findById(code).orElse(null);
    }

    @Transactional
    public boolean addNewDepartment(Department department) {
        try {
            departmentRepository.save(department);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to add new department: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateDepartmentByCode(String code, String name, String departmentHead, String email, String phone) {
        Optional<Department> opt = departmentRepository.findById(code);
        if (opt.isEmpty()) return false;
        Department department = opt.get();
        department.setName(name);
        department.setDepartmentHead(departmentHead);
        department.setEmail(email);
        department.setPhone(phone);
        try {
            departmentRepository.save(department);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to update department: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteDepartmentByCode(String code) {
        if (!departmentRepository.existsById(code)) return false;
        try {
            departmentRepository.deleteById(code);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete department: " + e.getMessage());
            return false;
        }
    }
}


