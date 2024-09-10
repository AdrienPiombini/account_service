package project.queries.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.queries.entities.AccountTransaction;

public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {

}
