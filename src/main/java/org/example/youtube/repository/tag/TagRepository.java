package org.example.youtube.repository.tag;

import org.example.youtube.entity.tag.TagEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends CrudRepository<TagEntity, Integer>,
        PagingAndSortingRepository<TagEntity, Integer> {
}
