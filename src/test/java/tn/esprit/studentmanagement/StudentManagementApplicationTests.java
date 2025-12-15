package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.DepartmentService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // This will use src/test/resources/application-test.properties
class StudentManagementApplicationTests {

    @Autowired
    private DepartmentService departmentService;

    @Test
    void testSaveAndReadDepartment() {
        // Create a new Department
        Department dep = new Department();
        dep.setName("Accounting");

        // Save it
        Department saved = departmentService.saveDepartment(dep);

        // Fetch it back
        Department fetched = departmentService.getDepartmentById(saved.getIdDepartment());

        // Assertions
        assertThat(fetched).isNotNull();
        assertThat(fetched.getName()).isEqualTo("Accounting");

        // Optional: clean up
        departmentService.deleteDepartment(fetched.getIdDepartment());
    }
}
