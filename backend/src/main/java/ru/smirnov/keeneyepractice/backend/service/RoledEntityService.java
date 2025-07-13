package ru.smirnov.keeneyepractice.backend.service;

import org.apache.commons.lang3.NotImplementedException;
import ru.smirnov.keeneyepractice.backend.dto.user.UserByPersonForUpdateDto;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Person;
import ru.smirnov.keeneyepractice.backend.exceptions.ServiceMethodNotImplementedException;
import ru.smirnov.keeneyepractice.backend.projection.UserByPersonProjection;

import java.util.List;
import java.util.Optional;


// вот такое ощущение, что лучше написать разные интерфейсы, чуть ли не с одним методом в каждом
// а потом создавать пустые интерфейсы, наследуя лишь те интерфейсы, которые нам нужны...
// и нужный сервис чтобы реализовывал только нужные интерфейсы
public interface RoledEntityService {

    default Long save(Person personToSave) throws ServiceMethodNotImplementedException {
        throw new ServiceMethodNotImplementedException(
                "Basically this method is not implemented"
        );
    }

    default List<UserByPersonProjection> findAll() throws ServiceMethodNotImplementedException {
        throw new ServiceMethodNotImplementedException(
                "Basically this method is not implemented"
        );
    }

    default Optional<UserByPersonProjection> findById(Long id) {
        throw new ServiceMethodNotImplementedException(
                "Basically this method is not implemented"
        );
    }

    default Person updateById(UserByPersonForUpdateDto dataForUpdate, Long id) {
        throw new ServiceMethodNotImplementedException(
                "Basically this method is not implemented"
        );
    }

}

// с чем ещё столкнулся...
// вот есть у меня в UserService метод, который в зависимости от роли, указанной в DTO,
// через enum понимает, в какой бизнес-таблице создавать запись. Для этого в сервисах, отвечающих
// за эти таблицы, реализован интерфейс с методом .save()

// но вот если я хочу обновить данные сразу и в Users, и в одной из бизнес-таблиц, то мне всё также нужно,
// чтобы я в зависимости от роли ...

// крч там была проблема того, что сервис, который в зависимости от роли делает что-то, вдруг должен начинать сам
// реализовывать все интерфейсы, и в конце концов может даже сам на себя сослаться