package org.example.youtube.repository;

import jakarta.transaction.Transactional;
import org.example.youtube.entity.ProfileEntity;
import org.example.youtube.enums.ProfileStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {
    Optional<ProfileEntity> findByEmailAndVisibleTrue(String username);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status =?2 where id =?1")
    int updateStatus(Integer profileId, ProfileStatus status);

    Optional<ProfileEntity> findByEmailAndPasswordAndVisibleIsTrue(String email, String password);

}
