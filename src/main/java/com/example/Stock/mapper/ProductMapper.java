package com.example.Stock.mapper;

import com.example.Stock.dto.*;
import com.example.Stock.entity.Category;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.Tag;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

/**
 * MapStruct mapper for converting between Product entities and DTOs.
 * Provides automatic mapping with custom configurations for complex relationships.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {CategoryMapper.class}
)
public interface ProductMapper {

    /**
     * Convert Product entity to ProductDTO.
     * 
     * @param product the entity to convert
     * @return the corresponding DTO
     */
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    ProductDTO toDTO(Product product);

    /**
     * Convert ProductDTO to Product entity.
     * 
     * @param productDTO the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "stockRecords", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "transfers", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    @Mapping(target = "inventoryLines", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    @Mapping(target = "recipeIngredients", ignore = true)
    Product toEntity(ProductDTO productDTO);

    /**
     * Convert ProductCreateDTO to Product entity.
     * 
     * @param createDTO the create DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    @Mapping(target = "stockRecords", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "transfers", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    @Mapping(target = "inventoryLines", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    @Mapping(target = "recipeIngredients", ignore = true)
    Product toEntity(ProductCreateDTO createDTO);

    /**
     * Update existing Product entity with data from ProductUpdateDTO.
     * 
     * @param updateDTO the update DTO with new data
     * @param product the existing entity to update
     */
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    @Mapping(target = "stockRecords", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "transfers", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    @Mapping(target = "inventoryLines", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    @Mapping(target = "recipeIngredients", ignore = true)
    void updateEntityFromDTO(ProductUpdateDTO updateDTO, @MappingTarget Product product);

    /**
     * Convert list of Product entities to list of ProductDTOs.
     * 
     * @param products the entities to convert
     * @return the corresponding DTOs
     */
    List<ProductDTO> toDTOList(List<Product> products);

    /**
     * Convert Tag entity to TagDTO.
     * 
     * @param tag the entity to convert
     * @return the corresponding DTO
     */
    TagDTO tagToTagDTO(Tag tag);

    /**
     * Convert list of Tag entities to list of TagDTOs.
     * 
     * @param tags the entities to convert
     * @return the corresponding DTOs
     */
    List<TagDTO> tagListToTagDTOList(List<Tag> tags);

    /**
     * Custom mapping method to convert Category ID to Category entity.
     * This method should be implemented in the service layer.
     * 
     * @param categoryId the category ID
     * @return the Category entity or null if ID is null
     */
    @Named("categoryIdToCategory")
    default Category categoryIdToCategory(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setCategoryId(categoryId);
        return category;
    }

    /**
     * Custom mapping method to convert list of Tag IDs to list of Tag entities.
     * This method should be implemented in the service layer.
     * 
     * @param tagIds the list of tag IDs
     * @return the list of Tag entities or null if list is null
     */
    @Named("tagIdsToTags")
    default List<Tag> tagIdsToTags(List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }
        return tagIds.stream()
                .map(tagId -> {
                    Tag tag = new Tag();
                    tag.setTagId(tagId);
                    return tag;
                })
                .toList();
    }
}
