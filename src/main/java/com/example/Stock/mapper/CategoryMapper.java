package com.example.Stock.mapper;

import com.example.Stock.dto.CategoryDTO;
import com.example.Stock.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper utility for converting between Category entity and CategoryDTO.
 * Provides clean separation between internal entity structure and API contracts.
 */
@Component
public class CategoryMapper {

    /**
     * Convert Category entity to CategoryDTO.
     * 
     * @param category the entity to convert
     * @return the corresponding DTO
     */
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        return new CategoryDTO(
            category.getCategoryId(),
            category.getName()
        );
    }

    /**
     * Convert CategoryDTO to Category entity.
     * 
     * @param categoryDTO the DTO to convert
     * @return the corresponding entity
     */
    public Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        
        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setName(categoryDTO.getName());
        
        return category;
    }

    /**
     * Convert list of Category entities to list of CategoryDTOs.
     * 
     * @param categories the entities to convert
     * @return the corresponding DTOs
     */
    public List<CategoryDTO> toDTOList(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of CategoryDTOs to list of Category entities.
     * 
     * @param categoryDTOs the DTOs to convert
     * @return the corresponding entities
     */
    public List<Category> toEntityList(List<CategoryDTO> categoryDTOs) {
        if (categoryDTOs == null) {
            return null;
        }
        
        return categoryDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
