package project.queries.services;


import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import project.commonApi.events.AccountCreatedEvent;
import project.queries.entities.Account;
import project.queries.queries.GetAllAccounts;
import project.queries.repository.AccountRepository;
import project.queries.repository.TransactionRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AccountEventHandlerService {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public AccountEventHandlerService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("*******");
        log.info("AccountRepository received");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getBalance());
        account.setStatus(event.getStatus());
        account.setCurrency(event.getCurrency());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccounts query) {
        return accountRepository.findAll();
    }
}
