package uz.tatu.repository.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Groups;
import uz.tatu.service.custom.ArticleListDTO;
import uz.tatu.service.custom.GroupListDTO;
import uz.tatu.service.utils.RequestUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubjectsGroupsRepositoryImpl {

    private final Logger log = LoggerFactory.getLogger(GroupsUsersRepositoryImpl.class);
    private final EntityManager entityManager;

    public SubjectsGroupsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<GroupListDTO> findAllGroups(Long id, Pageable pageable){

        Page<GroupListDTO> results = null;

        String jpqlQuery = "select g.id,\n" +
                "       g.name,\n" +
                "       g.created_at,\n" +
                "       g.updated_at,\n" +
                "       g.image_url\n" +
                "from subject_group sg\n" +
                "         left join groups g on g.id = sg.group_id\n" +
                "where 1 = 1 and sg.subject_id = :subjectId";

        try {
            Query query = entityManager.createNativeQuery(jpqlQuery, GroupListDTO.class);
            query.setParameter("subjectId", id);
            List<GroupListDTO> grids = query.getResultList();
            results = new PageImpl<>(grids, pageable, 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return results;
    }


    private void setProperty(Query query, MultiValueMap<String, String> queryParams) {

        if (RequestUtil.checkValue(queryParams, "subjectId")) {
            query.setParameter("subjectId", Long.valueOf(queryParams.getFirst("subjectId")));
        }


    }
    private StringBuilder getQueryBuilder(MultiValueMap<String, String> queryParams) {

        StringBuilder queryBuilder = new StringBuilder();

        if (RequestUtil.checkValueNumber(queryParams, "subjectId")) {
            queryBuilder.append(" AND sg.subjects_id = :subjectId ");
        }


        return queryBuilder;
    }
}
