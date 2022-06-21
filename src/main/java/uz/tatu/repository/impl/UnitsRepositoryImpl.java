package uz.tatu.repository.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Units;
import uz.tatu.service.custom.GroupListDTO;
import uz.tatu.service.custom.UnitsListDTO;
import uz.tatu.service.utils.RequestUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public class UnitsRepositoryImpl {
    private final Logger log = LoggerFactory.getLogger(GroupsUsersRepositoryImpl.class);
    private final EntityManager entityManager;

    public UnitsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<UnitsListDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParams){

        Page<UnitsListDTO> results = null;

        StringBuilder queryBuilder = getQueryBuilder(queryParams);

        String jpqlQuery = "select units.id,\n" +
                "       units.name_ru,\n" +
                "       units.name_en,\n" +
                "       units.name_uz,\n" +
                "       units.subjects_id\n" +
                "from units\n" +
                "where 1=1\n" + queryBuilder;

        try {
            Query query = entityManager.createNativeQuery(jpqlQuery, UnitsListDTO.class);
            setProperty(query, queryParams);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<UnitsListDTO> grids = query.getResultList();

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
        if (RequestUtil.checkValue(queryParams, "groupId")) {
            query.setParameter("groupId", Long.valueOf(queryParams.getFirst("groupId")));
        }

    }
    private StringBuilder getQueryBuilder(MultiValueMap<String, String> queryParams) {

        StringBuilder queryBuilder = new StringBuilder();

        if (RequestUtil.checkValueNumber(queryParams, "subjectId")) {
            queryBuilder.append(" AND units.subjects_id = :subjectId ");
        }
        if (RequestUtil.checkValueNumber(queryParams, "groupId")) {
            queryBuilder.append(" AND units.groups_id = :groupId ");
        }
        return queryBuilder;
    }
}
