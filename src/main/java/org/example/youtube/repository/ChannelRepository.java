package org.example.youtube.repository;
import org.example.youtube.entity.ChannelEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends CrudRepository<ChannelEntity, Integer> {

    Optional<ChannelEntity> findByIdAndStatusTrue(String id);


    // 5.
//    @Query(value = "select * from article as a\n" +
//            "inner join article_types as ats  on ats.article_id = a.id\n" +
//            "where types_id = ?1 and visible = true and status = 'PUBLISHED'\n" +
//            "order by created_date desc\n" +
//            "limit 5", nativeQuery = true)
//    List<ArticleEntity> getLast5ByTypesIdNative(Integer typesId);

    // 5.
//            "inner join ArticleTypesEntity as ats  on ats.articleId = a.id " +
//    @Query(value = "SELECT a.id,a.title,a.description,a.imageId,a.createdDate " +
//            " from ChannelEntity as a " +
//            "inner join a.articleTypes as ats " +
//            "where ats.typesId= ?1 and a.visible = true and a.status = 'PUBLISHED' " +
//            "order by a.createdDate desc " +
//            " limit ?2 ")
//    List<ArticleShortInfoMapper> getLastNByTypesId(Integer typesId, int limit);




    // 7. Get Last 8  Articles witch id not included in given list.
//    @Query(value = " SELECT a.id,a.title,a.description,a.imageId,a.createdDate " +
//            " from ArticleEntity as a " +
//            " where a.visible = true and a.status = 'PUBLISHED' " +
//            " and a.id not in ?1 " +
//            " order by a.createdDate desc " +
//            " limit 8 ")
//    List<ArticleShortInfoMapper> getLast8(List<String> ids);


//    @Query(value = "UPDATE ArticleEntity " +
//            "set ArticleEntity.status = ?2 " +
//            "where ArticleEntity .id = ?1 ")
//    void changeStatus(Integer id, String status);
}
