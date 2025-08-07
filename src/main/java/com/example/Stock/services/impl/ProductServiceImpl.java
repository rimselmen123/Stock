package com.example.Stock.services.impl;

import com.example.Stock.dto.ProductCreateDTO;
import com.example.Stock.dto.ProductDTO;
import com.example.Stock.dto.ProductUpdateDTO;
import com.example.Stock.entity.Category;
import com.example.Stock.entity.Product;
import com.example.Stock.entity.Tag;
import com.example.Stock.exception.DuplicateResourceException;
import com.example.Stock.exception.ResourceNotFoundException;
import com.example.Stock.exception.ValidationException;
import com.example.Stock.mapper.ProductMapper;
import com.example.Stock.repository.CategoryRepository;
import com.example.Stock.repository.ProductRepository;
import com.example.Stock.repository.TagRepository;
import com.example.Stock.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of ProductService providing business logic for product management.
 * Includes validation, exception handling, and transactional integrity for production use.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductCreateDTO createDTO) {
        log.debug("Creating new product: {}", createDTO.getName());
        
        validateProductCreateDTO(createDTO);
        
        // Check for duplicate name
        if (createDTO.getName() != null && productRepository.existsByName(createDTO.getName().trim())) {
            throw new DuplicateResourceException("Product", "name", createDTO.getName());
        }
        
        // Check for duplicate barcode
        if (createDTO.getBarcode() != null && !createDTO.getBarcode().trim().isEmpty() 
            && productRepository.existsByBarcode(createDTO.getBarcode().trim())) {
            throw new DuplicateResourceException("Product", "barcode", createDTO.getBarcode());
        }
        
        Product product = productMapper.toEntity(createDTO);
        
        // Set category if provided
        if (createDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", createDTO.getCategoryId()));
            product.setCategory(category);
        }
        
        // Set tags if provided
        if (createDTO.getTagIds() != null && !createDTO.getTagIds().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            for (UUID tagId : createDTO.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag", tagId));
                tags.add(tag);
            }
            product.setTags(tags);
        }
        
        // Trim string fields
        if (product.getName() != null) {
            product.setName(product.getName().trim());
        }
        if (product.getBarcode() != null) {
            product.setBarcode(product.getBarcode().trim());
        }
        if (product.getUnit() != null) {
            product.setUnit(product.getUnit().trim());
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("Successfully created product with ID: {}", savedProduct.getProductId());
        
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(UUID productId, ProductUpdateDTO updateDTO) {
        log.debug("Updating product with ID: {}", productId);
        
        if (productId == null) {
            throw new ValidationException("Product ID cannot be null");
        }
        
        validateProductUpdateDTO(updateDTO);
        
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        
        // Check for duplicate name (excluding current product)
        if (updateDTO.getName() != null) {
            Optional<Product> productWithSameName = productRepository.findByName(updateDTO.getName().trim());
            if (productWithSameName.isPresent() && !productWithSameName.get().getProductId().equals(productId)) {
                throw new DuplicateResourceException("Product", "name", updateDTO.getName());
            }
        }
        
        // Check for duplicate barcode (excluding current product)
        if (updateDTO.getBarcode() != null && !updateDTO.getBarcode().trim().isEmpty()) {
            Optional<Product> productWithSameBarcode = productRepository.findByBarcode(updateDTO.getBarcode().trim());
            if (productWithSameBarcode.isPresent() && !productWithSameBarcode.get().getProductId().equals(productId)) {
                throw new DuplicateResourceException("Product", "barcode", updateDTO.getBarcode());
            }
        }
        
        // Update basic fields
        productMapper.updateEntityFromDTO(updateDTO, existingProduct);
        
        // Update category if provided
        if (updateDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", updateDTO.getCategoryId()));
            existingProduct.setCategory(category);
        } else {
            existingProduct.setCategory(null);
        }
        
        // Update tags if provided
        if (updateDTO.getTagIds() != null) {
            if (updateDTO.getTagIds().isEmpty()) {
                existingProduct.setTags(new ArrayList<>());
            } else {
                List<Tag> tags = new ArrayList<>();
                for (UUID tagId : updateDTO.getTagIds()) {
                    Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag", tagId));
                    tags.add(tag);
                }
                existingProduct.setTags(tags);
            }
        }
        
        // Trim string fields
        if (existingProduct.getName() != null) {
            existingProduct.setName(existingProduct.getName().trim());
        }
        if (existingProduct.getBarcode() != null) {
            existingProduct.setBarcode(existingProduct.getBarcode().trim());
        }
        if (existingProduct.getUnit() != null) {
            existingProduct.setUnit(existingProduct.getUnit().trim());
        }
        
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Successfully updated product with ID: {}", productId);
        
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductById(UUID productId) {
        log.debug("Finding product by ID: {}", productId);
        
        if (productId == null) {
            return Optional.empty();
        }
        
        return productRepository.findById(productId)
            .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductByName(String name) {
        log.debug("Finding product by name: {}", name);
        
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return productRepository.findByName(name.trim())
            .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductByBarcode(String barcode) {
        log.debug("Finding product by barcode: {}", barcode);
        
        if (barcode == null || barcode.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return productRepository.findByBarcode(barcode.trim())
            .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.debug("Retrieving all products with pagination");
        
        Page<Product> products = productRepository.findAll(pageable);
        log.info("Found {} products", products.getTotalElements());
        
        return products.map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.debug("Retrieving all products");
        
        List<Product> products = productRepository.findAllByOrderByNameAsc();
        log.info("Found {} products", products.size());
        
        return productMapper.toDTOList(products);
    }

    @Override
    public void deleteProduct(UUID productId) {
        log.debug("Deleting product with ID: {}", productId);
        
        if (productId == null) {
            throw new ValidationException("Product ID cannot be null");
        }
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        
        // Check if product has stock before deletion
        List<Product> productsWithStock = productRepository.findProductsWithStock();
        if (productsWithStock.contains(product)) {
            throw new ValidationException("Cannot delete product '" + product.getName() + "' because it has stock records");
        }
        
        productRepository.deleteById(productId);
        log.info("Successfully deleted product with ID: {}", productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProductsByName(String query) {
        log.debug("Searching products by name with query: {}", query);
        
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        
        List<Product> products = productRepository.findByNameContainingIgnoreCase(query.trim());
        log.info("Found {} products matching name query: {}", products.size(), query);
        
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String searchText) {
        log.debug("Searching products by name or description with text: {}", searchText);
        
        if (searchText == null || searchText.trim().isEmpty()) {
            return getAllProducts();
        }
        
        List<Product> products = productRepository.searchByNameOrDescription(searchText.trim());
        log.info("Found {} products matching search text: {}", products.size(), searchText);
        
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(UUID categoryId) {
        log.debug("Finding products by category ID: {}", categoryId);
        
        if (categoryId == null) {
            return new ArrayList<>();
        }
        
        List<Product> products = productRepository.findByCategoryCategoryId(categoryId);
        log.info("Found {} products in category: {}", products.size(), categoryId);
        
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategoryName(String categoryName) {
        log.debug("Finding products by category name: {}", categoryName);
        
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Product> products = productRepository.findByCategoryName(categoryName.trim());
        log.info("Found {} products in category: {}", products.size(), categoryName);
        
        return productMapper.toDTOList(products);
    }

    private void validateProductCreateDTO(ProductCreateDTO createDTO) {
        if (createDTO == null) {
            throw new ValidationException("Product data cannot be null");
        }
        
        if (createDTO.getName() == null || createDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Product name is required");
        }
        
        if (createDTO.getName().trim().length() > 100) {
            throw new ValidationException("Product name must not exceed 100 characters");
        }
        
        if (createDTO.getBarcode() != null && createDTO.getBarcode().trim().length() > 50) {
            throw new ValidationException("Barcode must not exceed 50 characters");
        }
        
        if (createDTO.getUnit() != null && createDTO.getUnit().trim().length() > 20) {
            throw new ValidationException("Unit must not exceed 20 characters");
        }
    }

    private void validateProductUpdateDTO(ProductUpdateDTO updateDTO) {
        if (updateDTO == null) {
            throw new ValidationException("Product data cannot be null");
        }
        
        if (updateDTO.getName() == null || updateDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Product name is required");
        }
        
        if (updateDTO.getName().trim().length() > 100) {
            throw new ValidationException("Product name must not exceed 100 characters");
        }
        
        if (updateDTO.getBarcode() != null && updateDTO.getBarcode().trim().length() > 50) {
            throw new ValidationException("Barcode must not exceed 50 characters");
        }
        
        if (updateDTO.getUnit() != null && updateDTO.getUnit().trim().length() > 20) {
            throw new ValidationException("Unit must not exceed 20 characters");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByUnit(String unit) {
        log.debug("Finding products by unit: {}", unit);

        if (unit == null || unit.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Product> products = productRepository.findByUnit(unit.trim());
        log.info("Found {} products with unit: {}", products.size(), unit);

        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsWithStock() {
        log.debug("Finding products with stock");

        List<Product> products = productRepository.findProductsWithStock();
        log.info("Found {} products with stock", products.size());

        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsWithoutStock() {
        log.debug("Finding products without stock");

        List<Product> products = productRepository.findProductsWithoutStock();
        log.info("Found {} products without stock", products.size());

        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        log.debug("Checking if product exists with name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        return productRepository.existsByName(name.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByBarcode(String barcode) {
        log.debug("Checking if product exists with barcode: {}", barcode);

        if (barcode == null || barcode.trim().isEmpty()) {
            return false;
        }

        return productRepository.existsByBarcode(barcode.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByTag(UUID tagId) {
        log.debug("Finding products by tag ID: {}", tagId);

        if (tagId == null) {
            return new ArrayList<>();
        }

        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Tag", tagId));

        List<Product> products = productRepository.findByTag(tag);
        log.info("Found {} products with tag: {}", products.size(), tagId);

        return productMapper.toDTOList(products);
    }

    @Override
    public ProductDTO addTagsToProduct(UUID productId, List<UUID> tagIds) {
        log.debug("Adding tags to product with ID: {}", productId);

        if (productId == null) {
            throw new ValidationException("Product ID cannot be null");
        }

        if (tagIds == null || tagIds.isEmpty()) {
            throw new ValidationException("Tag IDs cannot be null or empty");
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        List<Tag> currentTags = product.getTags() != null ? new ArrayList<>(product.getTags()) : new ArrayList<>();

        for (UUID tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", tagId));

            // Add tag if not already present
            if (currentTags.stream().noneMatch(t -> t.getTagId().equals(tagId))) {
                currentTags.add(tag);
            }
        }

        product.setTags(currentTags);
        Product updatedProduct = productRepository.save(product);
        log.info("Successfully added tags to product with ID: {}", productId);

        return productMapper.toDTO(updatedProduct);
    }

    @Override
    public ProductDTO removeTagsFromProduct(UUID productId, List<UUID> tagIds) {
        log.debug("Removing tags from product with ID: {}", productId);

        if (productId == null) {
            throw new ValidationException("Product ID cannot be null");
        }

        if (tagIds == null || tagIds.isEmpty()) {
            throw new ValidationException("Tag IDs cannot be null or empty");
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        List<Tag> currentTags = product.getTags() != null ? new ArrayList<>(product.getTags()) : new ArrayList<>();

        // Remove specified tags
        currentTags.removeIf(tag -> tagIds.contains(tag.getTagId()));

        product.setTags(currentTags);
        Product updatedProduct = productRepository.save(product);
        log.info("Successfully removed tags from product with ID: {}", productId);

        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductEntityById(UUID productId) {
        log.debug("Finding product entity by ID: {}", productId);

        if (productId == null) {
            throw new ValidationException("Product ID cannot be null");
        }

        return productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
    }
}
