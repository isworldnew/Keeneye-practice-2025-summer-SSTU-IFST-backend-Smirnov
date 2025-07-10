package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.mapper.GroupMapper;
import ru.smirnov.keeneyepractice.backend.repository.GroupRepository;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }



    /* обычный boilerplate-код, даже без логики разделения по ролям */
}
