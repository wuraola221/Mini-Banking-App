package wura.project.mini_banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wura.project.mini_banking_app.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);
}
