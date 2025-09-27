package vn.iotstar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA tự động cung cấp các phương thức như:
    // save(), findById(), findAll(), deleteById(),...
    // Hiện tại chưa cần thêm phương thức tùy chỉnh.
}