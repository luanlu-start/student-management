package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.repository.DepartmentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<vn.edu.fpt.app.entities.Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentByCode(String code) {
        return departmentRepository.findById(code).orElse(null);
    }

    public boolean existsByCode(String code) {
        return code != null && departmentRepository.existsById(code.trim());
    }

    public boolean existsByName(String name) {
        return name != null && departmentRepository.existsByNameIgnoreCase(name.trim());
    }

    @Transactional
    public boolean addNewDepartment(Department department) {
        try {
            // saveAndFlush forces SQL execution here so constraint violations are catchable.
            departmentRepository.saveAndFlush(department);
            return true;
        } catch (DataIntegrityViolationException e) {
            System.out.println("Fail to add new department: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateDepartmentByCode(String code, String name, String departmentHead, String email, String phone) {
        Optional<Department> opt = departmentRepository.findById(code);
        if (opt.isEmpty()) return false;

        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) return false;
        if (departmentRepository.existsByNameIgnoreCaseAndCodeNot(normalizedName, code)) return false;

        Department department = opt.get();
        department.setName(normalizedName);
        department.setDepartmentHead(departmentHead);
        department.setEmail(email);
        department.setPhone(phone);
        try {
            departmentRepository.saveAndFlush(department);
            return true;
        } catch (DataIntegrityViolationException e) {
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
