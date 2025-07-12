package ru.smirnov.keeneyepractice.backend.service;

import org.apache.commons.lang3.NotImplementedException;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Person;
import ru.smirnov.keeneyepractice.backend.exceptions.ServiceMethodNotImplementedException;

public interface RoledEntityService {

    default Long save(Person personToSave) throws ServiceMethodNotImplementedException {
        throw new ServiceMethodNotImplementedException(
                "Basically this method is not implemented"
        );
    }

}

