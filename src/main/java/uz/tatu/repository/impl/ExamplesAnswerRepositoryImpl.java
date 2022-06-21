package uz.tatu.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.custom.ArticleListDTO;
import uz.tatu.service.custom.ExamplesAnswerListDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public class ExamplesAnswerRepositoryImpl {

    private final Logger log = LoggerFactory.getLogger(ExamplesAnswerRepositoryImpl.class);
    private final EntityManager entityManager;

    public ExamplesAnswerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Page<ExamplesAnswerListDTO> findAll(MultiValueMap<String , String> queryParam, Pageable pageable){
        Page<ExamplesAnswerListDTO> results = null;

        StringBuilder queryBuilder = getQueryBuilder(queryParam);

        String jpqlQuery = "select id            as id,\n" +
                "       name          as name,\n" +
                "       created_by_id as created_by,\n" +
                "       created_at    as created_at,\n" +
                "       examples_id   as examples_id,\n" +
                "       ball          as ball,\n" +
                "       files_id      as files_id\n" +
                "from example_answer\n" +
                "where 1 = 1\n" + queryBuilder;

        String jpqlCountQuery = "select count(id) as id\n" +
                "from example_answer\n" +
                "where 1 = 1\n" + queryBuilder;

        try {
            Query query = entityManager.createNativeQuery(jpqlQuery, ExamplesAnswerListDTO.class);
            setProperty(query, queryParam);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            Query countQuery = entityManager.createNativeQuery(jpqlCountQuery);
            setProperty(countQuery, queryParam);
            BigInteger count = (BigInteger) countQuery.getSingleResult();
            List<ExamplesAnswerListDTO> grids = query.getResultList();

            results = new PageImpl<>(grids, pageable, count.longValue());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return results;
    }


    private void setProperty(Query query, MultiValueMap<String, String> queryParams) {

    }
    private StringBuilder getQueryBuilder(MultiValueMap<String, String> queryParams) {
        StringBuilder queryBuilder = new StringBuilder();
        return queryBuilder;
    }
}
