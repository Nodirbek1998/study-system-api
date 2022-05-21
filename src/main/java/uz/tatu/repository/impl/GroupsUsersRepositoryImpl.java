package uz.tatu.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.dto.GroupsUsersDTO;

import javax.persistence.EntityManager;

@Repository
public class GroupsUsersRepositoryImpl {

    private final Logger log = LoggerFactory.getLogger(GroupsUsersRepositoryImpl.class);
    private final EntityManager entityManager;

    public GroupsUsersRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
