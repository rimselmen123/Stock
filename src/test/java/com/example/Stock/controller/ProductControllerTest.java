package com.example.Stock.controller;

import com.example.Stock.dto.ProductCreateDTO;
import com.example.Stock.dto.ProductDTO;
import com.example.Stock.dto.ProductUpdateDTO;
import com.example.Stock.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
 * Test class for ProductController.
 * Tests all REST endpoints to ensure proper functionality.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO testProductDTO;
    private ProductCreateDTO testCreateDTO;
    private ProductUpdateDTO testUpdateDTO;
    private UUID testProductId;

    @BeforeEach
    void setUp() {
        testProductId = UUID.randomUUID();
        
        testProductDTO = new ProductDTO();
        testProductDTO.setProductId(testProductId);
        testProductDTO.setName("Test Product");
        testProductDTO.setBarcode("1234567890");
        testProductDTO.setUnit("pieces");
        testProductDTO.setDescription("Test product description");

        testCreateDTO = new ProductCreateDTO();
        testCreateDTO.setName("Test Product");
        testCreateDTO.setBarcode("1234567890");
        testCreateDTO.setUnit("pieces");
        testCreateDTO.setDescription("Test product description");

        testUpdateDTO = new ProductUpdateDTO();
        testUpdateDTO.setName("Updated Product");
        testUpdateDTO.setBarcode("1234567890");
        testUpdateDTO.setUnit("pieces");
        testUpdateDTO.setDescription("Updated product description");
    }

    @Test
    void createProduct_Success() throws Exception {
        // Given
        when(productService.createProduct(any(ProductCreateDTO.class))).thenReturn(testProductDTO);

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.barcode").value("1234567890"))
                .andExpect(jsonPath("$.productId").value(testProductId.toString()));
    }

    @Test
    void createProduct_InvalidData() throws Exception {
        // Given
        ProductCreateDTO invalidDTO = new ProductCreateDTO();
        invalidDTO.setName(""); // Invalid empty name

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllProducts_WithPagination_Success() throws Exception {
        // Given
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        Page<ProductDTO> productPage = new PageImpl<>(products, PageRequest.of(0, 20), 1);
        
        when(productService.getAllProducts(any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Test Product"));
    }

    @Test
    void getAllProducts_NoPagination_Success() throws Exception {
        // Given
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void getProductById_Success() throws Exception {
        // Given
        when(productService.getProductById(testProductId)).thenReturn(Optional.of(testProductDTO));

        // When & Then
        mockMvc.perform(get("/api/products/{productId}", testProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.productId").value(testProductId.toString()));
    }

    @Test
    void getProductById_NotFound() throws Exception {
        // Given
        when(productService.getProductById(testProductId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/products/{productId}", testProductId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
    void updateProduct_Success() throws Exception {
        // Given
        ProductDTO updatedProduct = new ProductDTO();
        updatedProduct.setProductId(testProductId);
        updatedProduct.setName("Updated Product");
        
        when(productService.updateProduct(eq(testProductId), any(ProductUpdateDTO.class)))
                .thenReturn(updatedProduct);

        // When & Then
        mockMvc.perform(put("/api/products/{productId}", testProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void deleteProduct_Success() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/{productId}", testProductId))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchProductsByName_Success() throws Exception {
        // Given
        String query = "Test";
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        
        when(productService.searchProductsByName(query)).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/search/name")
                .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void searchProducts_Success() throws Exception {
        // Given
        String searchText = "Test";
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        
        when(productService.searchProducts(searchText)).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/search")
                .param("searchText", searchText))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void getProductByName_Success() throws Exception {
        // Given
        String productName = "Test Product";
        when(productService.getProductByName(productName)).thenReturn(Optional.of(testProductDTO));

        // When & Then
        mockMvc.perform(get("/api/products/name/{name}", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductByBarcode_Success() throws Exception {
        // Given
        String barcode = "1234567890";
        when(productService.getProductByBarcode(barcode)).thenReturn(Optional.of(testProductDTO));

        // When & Then
        mockMvc.perform(get("/api/products/barcode/{barcode}", barcode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value("1234567890"));
    }

    @Test
    void getProductsByCategory_Success() throws Exception {
        // Given
        UUID categoryId = UUID.randomUUID();
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        
        when(productService.getProductsByCategory(categoryId)).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void getProductsWithStock_Success() throws Exception {
        // Given
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        when(productService.getProductsWithStock()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/with-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void existsByName_Success() throws Exception {
        // Given
        String productName = "Test Product";
        when(productService.existsByName(productName)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/products/exists/name/{name}", productName))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsByBarcode_Success() throws Exception {
        // Given
        String barcode = "1234567890";
        when(productService.existsByBarcode(barcode)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/products/exists/barcode/{barcode}", barcode))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
