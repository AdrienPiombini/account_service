package project.commonApi.events;

import lombok.Getter;

public class AccountCreditedEvent extends BaseEvent<String> {

    @Getter
    private String currency;
    @Getter
    private double amount;

    public AccountCreditedEvent(String id, String currency, double balance) {
        super(id);
        this.currency = currency;
        this.amount = balance;
    }
}
