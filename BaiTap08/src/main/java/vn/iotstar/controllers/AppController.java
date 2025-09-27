package vn.iotstar.controllers;

import vn.iotstar.entities.Category;
import vn.iotstar.entities.Product;
import vn.iotstar.entities.User;
import vn.iotstar.repositories.CategoryRepository;
import vn.iotstar.repositories.ProductRepository;
import vn.iotstar.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// Khai báo Input Types để khớp với schema
// Bạn có thể tạo các file riêng cho chúng hoặc định nghĩa chúng ở đây
record UserInput(String fullname, String email, String password, String phone) {}
record CategoryInput(String name, String images) {}
record ProductInput(String title, int quantity, String description, double price, Long userId, List<Long> categoryIds) {}

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    // === QUERIES ===

    @QueryMapping
    public List<Product> productsByPriceAsc() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findByCategories_Id(categoryId);
    }

    @QueryMapping
    public Optional<Product> productById(@Argument Long id) {
        return productRepository.findById(id);
    }
    
    @QueryMapping
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    
    @QueryMapping
    public Optional<User> userById(@Argument Long id) {
        return userRepository.findById(id);
    }
    
    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }
    
    @QueryMapping
    public Optional<Category> categoryById(@Argument Long id) {
        return categoryRepository.findById(id);
    }

    // === MUTATIONS ===

    // -- User CRUD --
    @MutationMapping
    public User createUser(@Argument UserInput user) {
        User newUser = new User();
        newUser.setFullname(user.fullname());
        newUser.setEmail(user.email());
        newUser.setPassword(user.password()); // Nên mã hóa mật khẩu trong thực tế
        newUser.setPhone(user.phone());
        return userRepository.save(newUser);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument UserInput user) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        existingUser.setFullname(user.fullname());
        existingUser.setEmail(user.email());
        existingUser.setPassword(user.password());
        existingUser.setPhone(user.phone());
        return userRepository.save(existingUser);
    }

    @MutationMapping
    public boolean deleteUser(@Argument Long id) {
        userRepository.deleteById(id);
        return true;
    }

    // -- Category CRUD --
    @MutationMapping
    public Category createCategory(@Argument CategoryInput category) {
        Category newCategory = new Category();
        newCategory.setName(category.name());
        newCategory.setImages(category.images());
        return categoryRepository.save(newCategory);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument CategoryInput category) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        existingCategory.setName(category.name());
        existingCategory.setImages(category.images());
        return categoryRepository.save(existingCategory);
    }
    
    @MutationMapping
    public boolean deleteCategory(@Argument Long id) {
        categoryRepository.deleteById(id);
        return true;
    }

    // -- Product CRUD --
    @MutationMapping
    public Product createProduct(@Argument ProductInput product) {
        User user = userRepository.findById(product.userId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(product.categoryIds()));

        Product newProduct = new Product();
        newProduct.setTitle(product.title());
        newProduct.setPrice(product.price());
        newProduct.setQuantity(product.quantity());
        newProduct.setDescription(product.description());
        newProduct.setUser(user);
        newProduct.setCategories(categories);
        
        return productRepository.save(newProduct);
    }
    
    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        User user = userRepository.findById(product.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(product.categoryIds()));
        
        existingProduct.setTitle(product.title());
        existingProduct.setPrice(product.price());
        existingProduct.setQuantity(product.quantity());
        existingProduct.setDescription(product.description());
        existingProduct.setUser(user);
        existingProduct.setCategories(categories);
        
        return productRepository.save(existingProduct);
    }

    @MutationMapping
    public boolean deleteProduct(@Argument Long id) {
        productRepository.deleteById(id);
        return true;
    }
}