package wura.project.mini_banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wura.project.mini_banking_app.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
