package org.example.youtube.repository.category;

import org.example.youtube.entity.category.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer>,
        PagingAndSortingRepository<CategoryEntity, Integer> {
}
