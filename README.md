# Stock Management System - JPA Entity Layer

This project provides a complete JPA entity layer for a comprehensive stock management and inventory system built with Spring Boot and PostgreSQL.

## 📋 Schema Analysis Results

### ✅ Schema Strengths
- **Consistent UUID usage** - All primary keys use UUID with `gen_random_uuid()`
- **Proper foreign key relationships** - All references are correctly defined
- **Appropriate constraints** - CHECK constraints for enums, UNIQUE constraints where needed
- **Audit trails** - Timestamps for tracking creation and updates
- **Logical business model** - Well-structured inventory management system

### ⚠️ Handled Issues
- **Generated column** in `inventory_lines.difference` - Implemented as calculated method in Java
- **Composite primary key** in `product_tags` - Handled with `@JoinTable` mapping
- **Self-referencing constraints** in transfers table - Properly mapped with distinct relationships

## 🏗️ Entity Architecture

### Core Entities
1. **Location** - Physical storage locations
2. **Category** - Product categorization
3. **Supplier** - Vendor management
4. **User** - System users with role-based access
5. **Tag** - Flexible product tagging
6. **Product** - Core inventory items

### Transaction Entities
7. **Stock** - Current inventory levels
8. **Purchase** - Incoming inventory transactions
9. **Sale** - Outgoing sales transactions
10. **Transfer** - Inter-location movements
11. **StockMovement** - Complete audit trail

### Inventory Management
12. **InventorySession** - Physical counting sessions
13. **InventoryLine** - Individual count records

### Recipe Management
14. **Recipe** - Product assembly instructions
15. **RecipeIngredient** - Bill of materials

## 🔧 Technical Features

### JPA Annotations
- **UUID Primary Keys** with `@UuidGenerator`
- **Enum Mappings** with `@Enumerated(EnumType.STRING)`
- **Relationship Mappings** (`@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`)
- **Unique Constraints** with `@Table(uniqueConstraints = {...})`
- **Audit Timestamps** with `@PrePersist` and `@PreUpdate`

### Lombok Integration
- `@Data` for getters, setters, equals, hashCode, toString
- `@NoArgsConstructor` and `@AllArgsConstructor`
- Clean, maintainable code

### Spring Data JPA Repositories
- Complete repository interfaces for all entities
- Custom query methods with `@Query`
- Business-specific finder methods
- Aggregation and calculation methods

## 📁 Project Structure

```
src/main/java/com/example/Stock/
├── entity/
│   ├── Location.java
│   ├── Category.java
│   ├── Supplier.java
│   ├── User.java
│   ├── UserRole.java (enum)
│   ├── Tag.java
│   ├── Product.java
│   ├── Log.java
│   ├── Stock.java
│   ├── Purchase.java
│   ├── Sale.java
│   ├── Transfer.java
│   ├── StockMovement.java
│   ├── MovementType.java (enum)
│   ├── InventorySession.java
│   ├── InventoryStatus.java (enum)
│   ├── InventoryLine.java
│   ├── Recipe.java
│   └── RecipeIngredient.java
└── repository/
    ├── LocationRepository.java
    ├── CategoryRepository.java
    ├── SupplierRepository.java
    ├── UserRepository.java
    ├── TagRepository.java
    ├── ProductRepository.java
    ├── LogRepository.java
    ├── StockRepository.java
    ├── PurchaseRepository.java
    ├── SaleRepository.java
    ├── TransferRepository.java
    ├── StockMovementRepository.java
    ├── InventorySessionRepository.java
    ├── InventoryLineRepository.java
    ├── RecipeRepository.java
    └── RecipeIngredientRepository.java
```

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Dependencies
The project includes all necessary dependencies:
- Spring Boot Starter Data JPA
- PostgreSQL Driver
- Lombok
- H2 Database (for testing)

### Database Setup
1. Create a PostgreSQL database named `Stock`
2. Update `application.properties` with your database credentials
3. Run the application - Hibernate will create the schema automatically

### Running Tests
```bash
mvn test
```

## 📊 Key Relationships

### Product-Centric Relationships
- **Product ↔ Category**: Many-to-One
- **Product ↔ Tags**: Many-to-Many
- **Product ↔ Stock**: One-to-Many (per location)

### Transaction Relationships
- **Purchase**: Product + Supplier + Location
- **Sale**: Product + Location + User
- **Transfer**: Product + From Location + To Location + User

### Inventory Management
- **InventorySession**: Location + User + Multiple InventoryLines
- **InventoryLine**: Session + Product + Expected/Counted quantities

### Recipe System
- **Recipe**: One-to-One with Product
- **RecipeIngredient**: Recipe + Ingredient Product + Quantity

## 🔍 Advanced Features

### Calculated Fields
- **InventoryLine.getDifference()**: Automatic discrepancy calculation
- **RecipeIngredient.getTotalCost()**: Cost calculation methods
- **Stock movement aggregations**: Total quantities and values

### Audit Trail
- **StockMovement**: Complete history of all inventory changes
- **Log**: User activity tracking
- **Timestamps**: Creation and update tracking

### Business Logic Support
- **Low stock detection**: Repository methods for inventory alerts
- **Sales analytics**: Top products by quantity/revenue
- **Cost calculations**: Recipe costing and purchase analysis

## 🧪 Testing

The project includes comprehensive tests:
- **EntityMappingTest**: Verifies JPA mappings and basic operations
- **H2 in-memory database**: Fast test execution
- **Test profiles**: Separate configuration for testing

## 📈 Performance Considerations

- **Lazy Loading**: All relationships use `FetchType.LAZY`
- **Indexed Columns**: Unique constraints create indexes automatically
- **Efficient Queries**: Custom repository methods for common operations
- **Batch Operations**: Support for bulk operations where appropriate

## 🔒 Security Features

- **User Roles**: ADMIN, MANAGER, CASHIER with enum validation
- **Audit Logging**: Complete user activity tracking
- **Data Integrity**: Foreign key constraints and validation

## 🛠️ Customization

### Adding New Entities
1. Create entity class with proper JPA annotations
2. Create corresponding repository interface
3. Add relationships to existing entities if needed
4. Update tests

### Extending Functionality
- Add custom query methods to repositories
- Implement business logic in service layer
- Add validation annotations as needed

## 📝 Notes

- All entities use UUID primary keys for better distribution and security
- Enum values are stored as strings for better readability
- Timestamps are automatically managed by JPA lifecycle callbacks
- The schema supports multi-location inventory management
- Recipe system enables bill-of-materials functionality

## 🤝 Contributing

1. Follow the established naming conventions
2. Add proper JavaDoc comments
3. Include unit tests for new functionality
4. Maintain consistent code style with Lombok annotations

## 📄 License

This project is part of a stock management system implementation.
