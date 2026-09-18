package wura.project.mini_banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wura.project.mini_banking_app.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(
            Long accountId
    );

    boolean existsByReference(String reference);
}
