package com.example.Stock.entity;

import com.example.Stock.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class to verify JPA entity mappings and basic repository operations.
 * This test ensures that all entities are properly configured and can be
 * persisted and retrieved from the database.
 */
@DataJpaTest
@ActiveProfiles("test")
class EntityMappingTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Test
    void testLocationEntityMapping() {
        // Given
        Location location = new Location();
        location.setName("Main Warehouse");
        location.setAddress("123 Storage Street, City");

        // When
        Location savedLocation = locationRepository.save(location);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedLocation.getLocationId()).isNotNull();
        assertThat(savedLocation.getName()).isEqualTo("Main Warehouse");
        assertThat(savedLocation.getAddress()).isEqualTo("123 Storage Street, City");

        Location foundLocation = locationRepository.findById(savedLocation.getLocationId()).orElse(null);
        assertThat(foundLocation).isNotNull();
        assertThat(foundLocation.getName()).isEqualTo("Main Warehouse");
    }

    @Test
    void testCategoryEntityMapping() {
        // Given
        Category category = new Category();
        category.setName("Electronics");

        // When
        Category savedCategory = categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedCategory.getCategoryId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Electronics");

        Category foundCategory = categoryRepository.findByName("Electronics").orElse(null);
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("Electronics");
    }

    @Test
    void testUserEntityMapping() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedpassword");
        user.setRole(UserRole.CASHIER);

        // When
        User savedUser = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.CASHIER);
        assertThat(savedUser.getCreatedAt()).isNotNull();

        User foundUser = userRepository.findByUsername("testuser").orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getRole()).isEqualTo(UserRole.CASHIER);
    }

    @Test
    void testProductWithCategoryMapping() {
        // Given
        Category category = new Category();
        category.setName("Food");
        Category savedCategory = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Apple");
        product.setBarcode("1234567890");
        product.setUnit("kg");
        product.setDescription("Fresh red apples");
        product.setCategory(savedCategory);

        // When
        Product savedProduct = productRepository.save(product);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedProduct.getProductId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Apple");
        assertThat(savedProduct.getBarcode()).isEqualTo("1234567890");

        Product foundProduct = productRepository.findByBarcode("1234567890").orElse(null);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getCategory()).isNotNull();
        assertThat(foundProduct.getCategory().getName()).isEqualTo("Food");
    }

    @Test
    void testStockEntityMapping() {
        // Given
        Location location = new Location();
        location.setName("Store Front");
        Location savedLocation = locationRepository.save(location);

        Category category = new Category();
        category.setName("Beverages");
        Category savedCategory = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Water Bottle");
        product.setCategory(savedCategory);
        Product savedProduct = productRepository.save(product);

        Stock stock = new Stock();
        stock.setProduct(savedProduct);
        stock.setLocation(savedLocation);
        stock.setQuantity(100);

        // When
        Stock savedStock = stockRepository.save(stock);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedStock.getStockId()).isNotNull();
        assertThat(savedStock.getQuantity()).isEqualTo(100);
        assertThat(savedStock.getLastUpdated()).isNotNull();

        Stock foundStock = stockRepository.findByProductAndLocation(savedProduct, savedLocation).orElse(null);
        assertThat(foundStock).isNotNull();
        assertThat(foundStock.getQuantity()).isEqualTo(100);
    }

    @Test
    void testEnumMappings() {
        // Test UserRole enum
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");
        user.setRole(UserRole.ADMIN);
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getRole()).isEqualTo(UserRole.ADMIN);

        // Test MovementType enum
        StockMovement movement = new StockMovement();
        movement.setMovementType(MovementType.PURCHASE);
        movement.setQuantityChange(50);

        assertThat(movement.getMovementType()).isEqualTo(MovementType.PURCHASE);

        // Test InventoryStatus enum
        InventorySession session = new InventorySession();
        session.setStatus(InventoryStatus.OPEN);

        assertThat(session.getStatus()).isEqualTo(InventoryStatus.OPEN);
    }
}
