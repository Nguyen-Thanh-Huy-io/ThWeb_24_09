package vn.iotstar.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Phương thức tùy chỉnh để tìm tất cả sản phẩm thuộc về một danh mục cụ thể
     * dựa vào categoryId.
     * @param categoryId ID của danh mục cần tìm sản phẩm.
     * @return Danh sách các sản phẩm thuộc danh mục đó.
     */
    List<Product> findByCategories_Id(Long categoryId);
}