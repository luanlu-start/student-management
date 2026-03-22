package vn.edu.fpt.app.service;

import vn.edu.fpt.app.dto.DepartmentDTO;
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

    // thêm mới department
    @Transactional
    public void add(DepartmentDTO dto) {

        if (departmentRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("CODE_EXISTS");
        }

        if (departmentRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("NAME_EXISTS");
        }

        Department dep = new Department(
                dto.getCode(),
                dto.getName(),
                dto.getHead(),
                dto.getPhone(),
                dto.getEmail()
        );

        departmentRepository.save(dep);
    }


    @Transactional
    public void update(DepartmentDTO dto) {

        Department department = departmentRepository.findById(dto.getCode())
                .orElseThrow(() -> new RuntimeException("NOT_FOUND"));

        String name = dto.getName().trim();

        if (departmentRepository.existsByNameIgnoreCaseAndCodeNot(name, dto.getCode())) {
            throw new RuntimeException("NAME_EXISTS");
        }

        department.setName(name);
        department.setDepartmentHead(dto.getHead());
        department.setEmail(dto.getEmail());
        department.setPhone(dto.getPhone());

        departmentRepository.save(department);
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
