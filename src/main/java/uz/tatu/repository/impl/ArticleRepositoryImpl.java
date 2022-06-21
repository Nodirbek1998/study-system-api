package uz.tatu.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.custom.ArticleListDTO;
import uz.tatu.service.utils.RequestUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public class ArticleRepositoryImpl {

    private final Logger log = LoggerFactory.getLogger(GroupsUsersRepositoryImpl.class);
    private final EntityManager entityManager;

    public ArticleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<ArticleListDTO> findAll(MultiValueMap<String , String> queryParam, Pageable pageable){
        Page<ArticleListDTO> results = null;

        StringBuilder queryBuilder = getQueryBuilder(queryParam);

        String jpqlQuery = "select a.id as id,\n" +
                "       a.created_at as created_at,\n" +
                "       ju.first_name as first_name,\n" +
                "       a.body as body,\n" +
                "       a.status as status,\n" +
                "       a.name as name,\n" +
                "       a.text as text,\n" +
                "       i.id as images_id\n" +
                " from article a\n" +
                "    left join images i on i.id = a.images_id\n" +
                "    left join jhi_user ju on ju.id = a.created_by_id\n" +
                "where 1 = 1" + queryBuilder;

        String jpqlCountQuery = "select count(a.id)\n" +
                " from article a\n" +
                "    left join images i on i.id = a.images_id\n" +
                "    left join jhi_user ju on ju.id = a.created_by_id\n" +
                "where 1 = 1\n" + queryBuilder;

        try {
            Query query = entityManager.createNativeQuery(jpqlQuery, ArticleListDTO.class);
            setProperty(query, queryParam);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            Query countQuery = entityManager.createNativeQuery(jpqlCountQuery);
            setProperty(countQuery, queryParam);
            BigInteger count = (BigInteger) countQuery.getSingleResult();
            List<ArticleListDTO> grids = query.getResultList();

            results = new PageImpl<>(grids, pageable, count.longValue());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return results;
    }


    private void setProperty(Query query, MultiValueMap<String, String> queryParams) {
        if (RequestUtil.checkValue(queryParams, "status")) {
            query.setParameter("status", queryParams.getFirst("status"));
        }
    }
    private StringBuilder getQueryBuilder(MultiValueMap<String, String> queryParams) {
        StringBuilder queryBuilder = new StringBuilder();

        if (RequestUtil.checkValue(queryParams, "status")) {
            queryBuilder.append(" AND a.status = :status ");
        }
        return queryBuilder;
    }
}
