package com.example.Stock.controller;

import com.example.Stock.dto.CategoryDTO;
import com.example.Stock.entity.Category;
import com.example.Stock.mapper.CategoryMapper;
import com.example.Stock.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for CategoryController.
 * Tests all REST endpoints to ensure proper functionality.
 */
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryMapper categoryMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory;
    private CategoryDTO testCategoryDTO;
    private UUID testCategoryId;

    @BeforeEach
    void setUp() {
        testCategoryId = UUID.randomUUID();
        
        testCategory = new Category();
        testCategory.setCategoryId(testCategoryId);
        testCategory.setName("Electronics");

        testCategoryDTO = new CategoryDTO();
        testCategoryDTO.setCategoryId(testCategoryId);
        testCategoryDTO.setName("Electronics");
    }

    @Test
    void createCategory_Success() throws Exception {
        // Given
        CategoryDTO createDTO = new CategoryDTO("Electronics");
        when(categoryMapper.toEntity(any(CategoryDTO.class))).thenReturn(testCategory);
        when(categoryService.saveCategory(any(Category.class))).thenReturn(testCategory);
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(testCategoryDTO);

        // When & Then
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.categoryId").value(testCategoryId.toString()));
    }

    @Test
    void createCategory_InvalidData() throws Exception {
        // Given
        CategoryDTO invalidDTO = new CategoryDTO("");

        // When & Then
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCategories_Success() throws Exception {
        // Given
        List<Category> categories = Arrays.asList(testCategory);
        List<CategoryDTO> categoryDTOs = Arrays.asList(testCategoryDTO);
        
        when(categoryService.getAllCategories()).thenReturn(categories);
        when(categoryMapper.toDTOList(categories)).thenReturn(categoryDTOs);

        // When & Then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategoryById_Success() throws Exception {
        // Given
        when(categoryService.getCategoryById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(categoryMapper.toDTO(testCategory)).thenReturn(testCategoryDTO);

        // When & Then
        mockMvc.perform(get("/api/categories/{categoryId}", testCategoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.categoryId").value(testCategoryId.toString()));
    }

    @Test
    void getCategoryById_NotFound() throws Exception {
        // Given
        when(categoryService.getCategoryById(testCategoryId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/categories/{categoryId}", testCategoryId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category not found"));
    }

    @Test
    void updateCategory_Success() throws Exception {
        // Given
        CategoryDTO updateDTO = new CategoryDTO("Updated Electronics");
        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(testCategoryId);
        updatedCategory.setName("Updated Electronics");
        
        CategoryDTO updatedDTO = new CategoryDTO(testCategoryId, "Updated Electronics");

        when(categoryMapper.toEntity(any(CategoryDTO.class))).thenReturn(updatedCategory);
        when(categoryService.updateCategory(eq(testCategoryId), any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDTO(updatedCategory)).thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(put("/api/categories/{categoryId}", testCategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Electronics"));
    }

    @Test
    void deleteCategory_Success() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/categories/{categoryId}", testCategoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchCategories_Success() throws Exception {
        // Given
        String query = "Elec";
        List<Category> categories = Arrays.asList(testCategory);
        List<CategoryDTO> categoryDTOs = Arrays.asList(testCategoryDTO);
        
        when(categoryService.searchCategories(query)).thenReturn(categories);
        when(categoryMapper.toDTOList(categories)).thenReturn(categoryDTOs);

        // When & Then
        mockMvc.perform(get("/api/categories/search")
                .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategoryByName_Success() throws Exception {
        // Given
        String categoryName = "Electronics";
        when(categoryService.getCategoryByName(categoryName)).thenReturn(Optional.of(testCategory));
        when(categoryMapper.toDTO(testCategory)).thenReturn(testCategoryDTO);

        // When & Then
        mockMvc.perform(get("/api/categories/name/{name}", categoryName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void existsByName_Success() throws Exception {
        // Given
        String categoryName = "Electronics";
        when(categoryService.existsByName(categoryName)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/categories/exists/{name}", categoryName))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getCategoriesWithProducts_Success() throws Exception {
        // Given
        List<Category> categories = Arrays.asList(testCategory);
        List<CategoryDTO> categoryDTOs = Arrays.asList(testCategoryDTO);
        
        when(categoryService.getCategoriesWithProducts()).thenReturn(categories);
        when(categoryMapper.toDTOList(categories)).thenReturn(categoryDTOs);

        // When & Then
        mockMvc.perform(get("/api/categories/with-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }
}
