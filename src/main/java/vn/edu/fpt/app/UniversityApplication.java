package vn.edu.fpt.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.edu.fpt.app.entities.Department;
import vn.edu.fpt.app.service.DepartmentService;

import java.util.List;

@SpringBootApplication
public class UniversityApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityApplication.class, args);
        ApplicationContext context =
                SpringApplication.run(UniversityApplication.class, args);

        DepartmentService service = context.getBean(DepartmentService.class);

        List<Department> list = service.getAll();

        System.out.println("Departments: " + list.size());

        for (Department d : list) {
            System.out.println(d.getCode() + " - " + d.getName());
        }

    }
}
