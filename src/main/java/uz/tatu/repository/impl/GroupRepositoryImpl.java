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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class GroupRepositoryImpl {

    private final Logger log = LoggerFactory.getLogger(GroupsUsersRepositoryImpl.class);
    private final EntityManager entityManager;

    public GroupRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<GroupListDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParams){

        Page<GroupListDTO> results = null;

        StringBuilder queryBuilder = getQueryBuilder(queryParams);

        String jpqlQuery = "select distinct(groups.id),\n" +
                "        groups.name,\n" +
                "        groups.created_at,\n" +
                "        groups.updated_at,\n" +
                "        groups.image_url\n" +
                "        from groups\n" +
                "    left join groups_users gu on groups.id = gu.groups_id\n" +
                "where 1 = 1" + queryBuilder;

        String jpqlCountQuery = "select count(groups.id)\n" +
                "        from groups\n" +
                "    left join groups_users gu on groups.id = gu.groups_id\n" +
                "where 1 = 1\n" + queryBuilder;

        try {
            Query query = entityManager.createNativeQuery(jpqlQuery, GroupListDTO.class);
            setProperty(query, queryParams);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            Query countQuery = entityManager.createNativeQuery(jpqlCountQuery);
            setProperty(countQuery, queryParams);
            BigInteger count = (BigInteger) countQuery.getSingleResult();
            List<GroupListDTO> grids = query.getResultList();

            results = new PageImpl<>(grids, pageable, count.longValue());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return results;
    }


    private void setProperty(Query query, MultiValueMap<String, String> queryParams) {

        if (RequestUtil.checkValue(queryParams, "userId")) {
            query.setParameter("userId", Long.valueOf(queryParams.getFirst("userId")));
        }
        if (RequestUtil.checkValue(queryParams, "userType")) {
            query.setParameter("userType", queryParams.getFirst("userType"));
        }

    }
    private StringBuilder getQueryBuilder(MultiValueMap<String, String> queryParams) {

        StringBuilder queryBuilder = new StringBuilder();

        if (RequestUtil.checkValueNumber(queryParams, "userId")) {
            queryBuilder.append(" AND gu.user_id = :userId ");
        }

        if (RequestUtil.checkValue(queryParams, "regDateBegin")) {
            queryBuilder.append(" AND gu.user_type = :userType");
        }
        return queryBuilder;
    }
}
