package uz.tatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.GroupsUsers;

@Repository
public interface GroupsUsersRepository extends JpaRepository<GroupsUsers, Long> {

    Page<GroupsUsers> findByGroups_Id(Long groupId, Pageable pageable);

}
