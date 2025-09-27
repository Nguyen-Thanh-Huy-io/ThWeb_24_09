package vn.iotstar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Các phương thức CRUD cơ bản đã có sẵn.
}