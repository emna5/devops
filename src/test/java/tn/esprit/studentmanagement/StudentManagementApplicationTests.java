package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.DepartmentService;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(DepartmentService.class)
@ActiveProfiles("test")
class StudentManagementApplicationTests {

    @Autowired
    private DepartmentService departmentService;

    @Test
    void testSaveAndReadDepartment() {
        Department d = new Department();
        d.setName("IT");
        d.setLocation("Block A");
        d.setPhone("123456");
        d.setHead("Admin");

        Department saved = departmentService.saveDepartment(d);

        assertNotNull(saved.getIdDepartment());
        assertEquals("IT", saved.getName());

        Department found =
                departmentService.getDepartmentById(saved.getIdDepartment());

        assertNotNull(found);
        assertEquals("IT", found.getName());
    }
}
