package org.example.youtube.repository;

import org.example.youtube.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {
    Optional<ProfileEntity> findByEmailAndVisibleTrue(String username);
}
