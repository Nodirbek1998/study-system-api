package uz.tatu.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.custom.GroupUserListDTO;
import uz.tatu.service.dto.GroupsUsersDTO;
import uz.tatu.service.utils.RequestUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GroupsUsersRepositoryImpl {

    private final Logger log = LoggerFactory.getLogger(GroupsUsersRepositoryImpl.class);
    private final EntityManager entityManager;

    public GroupsUsersRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<GroupUserListDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParams){

        List<GroupUserListDTO> results = null;

        StringBuilder queryBuilder = getQueryBuilder(queryParams);

        String jpqlQuery = "select ju.id,\n" +
                "       ju.first_name,\n" +
                "       ju.last_name,\n" +
                "       ju.phone,\n" +
                "       ju.email\n" +
                "           from groups_users gu\n" +
                "    left join  jhi_user ju on gu.users_id = ju.id\n" +
                "where 1=1 \n" + queryBuilder;

        try {
            Query query = entityManager.createNativeQuery(jpqlQuery, GroupUserListDTO.class);
            setProperty(query, queryParams);

            results = query.getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return results;
    }

    private void setProperty(Query query, MultiValueMap<String, String> queryParams) {

        if (RequestUtil.checkValue(queryParams, "groupId")) {
            query.setParameter("groupId", Long.valueOf(queryParams.getFirst("groupId")));
        }
        if (RequestUtil.checkValue(queryParams, "userType")) {
            query.setParameter("userType", queryParams.getFirst("userType"));
        }

    }
    private StringBuilder getQueryBuilder(MultiValueMap<String, String> queryParams) {

        StringBuilder queryBuilder = new StringBuilder();

        if (RequestUtil.checkValueNumber(queryParams, "groupId")) {
            queryBuilder.append(" AND gu.groups_id = :groupId ");
        }

        if (RequestUtil.checkValue(queryParams, "userType")) {
            queryBuilder.append(" AND gu.user_type = :userType");
        }
        return queryBuilder;
    }

}
