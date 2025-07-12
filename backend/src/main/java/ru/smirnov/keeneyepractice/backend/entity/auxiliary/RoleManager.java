package ru.smirnov.keeneyepractice.backend.entity.auxiliary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.keeneyepractice.backend.exceptions.NoSuchRoleException;
import ru.smirnov.keeneyepractice.backend.exceptions.ServiceMethodNotImplementedException;
import ru.smirnov.keeneyepractice.backend.service.AdminService;
import ru.smirnov.keeneyepractice.backend.service.RoledEntityService;
import ru.smirnov.keeneyepractice.backend.service.StudentService;
import ru.smirnov.keeneyepractice.backend.service.TeacherService;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleManager {

    private final Map<Role, RoleAdapter> adaptedRoles = new HashMap<>();

    @Autowired
    public RoleManager(
            StudentService studentService,
            TeacherService teacherService,
            AdminService adminService
    ) {
        this.adaptedRoles.put(Role.STUDENT, new RoleAdapter(studentService));
        this.adaptedRoles.put(Role.TEACHER, new RoleAdapter(teacherService));
        this.adaptedRoles.put(Role.ADMIN, new RoleAdapter(adminService));
    }

    public RoledEntityService valueOf(String value) /*throws NoSuchRoleException*/ {

        // специально у inner класса сделал интерфейсную совместимость, чтобы в данном методе
        // не возвращать ссылку типа RoleAdapter, а вернуть исключительно "интерфейсную составляющую этого объекта"

        try {
            return this.adaptedRoles.get(Role.valueOf(value));
        }
        catch (IllegalArgumentException iae) {
            throw new NoSuchRoleException("Invalid role name: " + value);
        }


    }


    private class RoleAdapter implements RoledEntityService {

        private final RoledEntityService service;

        public RoleAdapter(RoledEntityService service) {
            this.service = service;
        }

        @Override
        public Long save(Person personToSave) throws ServiceMethodNotImplementedException {
            return this.service.save(personToSave);
        }
    }

}
