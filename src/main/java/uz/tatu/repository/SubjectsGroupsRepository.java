package uz.tatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.SubjectsGroups;

@Repository
public interface SubjectsGroupsRepository extends JpaRepository<SubjectsGroups, Long> {

}
