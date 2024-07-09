package org.example.youtube.repository;

import jakarta.transaction.Transactional;
import org.example.youtube.entity.PlayListEntity;
import org.example.youtube.enums.PlaylistStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<PlayListEntity, Long> {
    @Transactional
    @Modifying
    @Query("update PlayListEntity set status = ?2 where id = ?1")
    void updateStatus(Long playlistId, PlaylistStatus status);

    Page<PlayListEntity> findAllBy(Pageable pageable);

    @Query("select count (p) from PlayListEntity  p ")
    int videoCount();

    @Query( " SELECT playlist " +
            " FROM PlayListEntity AS playlist " +
            " JOIN FETCH playlist.chanel AS channel " +
            " JOIN FETCH channel.profile AS profile " +
            " WHERE profile.id = :userId")
    List<PlayListEntity> getByUserId(Long userId);

    List<PlayListEntity> findAllByChanelId(String chanelId);


}

