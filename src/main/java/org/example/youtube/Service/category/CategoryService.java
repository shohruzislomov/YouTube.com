package org.example.youtube.Service.category;

import lombok.RequiredArgsConstructor;
import org.example.youtube.dto.category.CategoryCreateDTO;
import org.example.youtube.dto.category.CategoryDTO;
import org.example.youtube.entity.category.CategoryEntity;
import org.example.youtube.exp.AppBadException;
import org.example.youtube.repository.category.CategoryRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryCreateDTO categoryDTO) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(categoryDTO.getName());
        categoryRepository.save(entity);
        return toDTO(entity);
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public Boolean update(Integer id, CategoryCreateDTO dto) {
        CategoryEntity entity = getCategory(id);
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        return true;
    }

    public CategoryEntity getCategory(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new AppBadException("Category not found"));
    }

    public Boolean delete(Integer id) {
        CategoryEntity entity = getCategory(id);
        entity.setVisible(Boolean.FALSE);
        categoryRepository.save(entity);
        return true;
    }

    public PageImpl<CategoryDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<CategoryEntity> mapperList = categoryRepository.findAll(pageable);
        return new PageImpl<>(iterateStream(mapperList.getContent()), mapperList.getPageable(), mapperList.getTotalElements());
    }

    private List<CategoryDTO> iterateStream(List<CategoryEntity> categories) {
        return categories.stream()
                .map(this::toDTO)
                .toList();
    }


}
