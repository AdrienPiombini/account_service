package project.commands.controllers.aggregates;


import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import project.commonApi.commands.CreateAccountCommand;
import project.commonApi.commands.CreditAccountCommand;
import project.commonApi.commands.DebitAccountCommand;
import project.commonApi.enums.AccountStatus;
import project.commonApi.events.AccountCreatedEvent;
import project.commonApi.events.AccountCreditedEvent;
import project.commonApi.events.AccountDebitedEvent;
import project.commonApi.exceptions.NegativeInitialBalanceException;

@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private String currency;
    private double balance;
    private AccountStatus status;


    public AccountAggregate() {
        //required for AXON
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        if (command.getInitialBalance() < 0) throw new NegativeInitialBalanceException("Negative balance");
        AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), command.getCurrency(), command.getInitialBalance(), AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.balance = event.getBalance();
        this.status = event.getStatus();
        this.currency = event.getCurrency();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        if (command.getAmount() < 0) throw new NegativeInitialBalanceException("Negative Amount");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getCurrency(),
                command.getAmount()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        if (command.getAmount() < 0) throw new NegativeInitialBalanceException("Negative Amount");
        if (command.getAmount() > this.balance) throw new RuntimeException("Balance insufficient Exception");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getCurrency(),
                command.getAmount()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }
}
