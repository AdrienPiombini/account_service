package project.queries.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.queries.entities.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
    
}
