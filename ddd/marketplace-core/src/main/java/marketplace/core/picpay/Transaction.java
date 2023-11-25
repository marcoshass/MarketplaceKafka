package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import marketplace.shared.AggregateRoot;

public class Transaction extends AggregateRoot<Integer> {
    private int id;
    private boolean isAuthorized;

    private UserInfo payer;
    private UserInfo payee;
    private double amount;

    private int payerId;
    private int payeeId;

    public Transaction(int id, UserInfo payer, UserInfo payee,
                       double amount, boolean isAuthorized) {

        this.isAuthorized = isAuthorized;

        apply(o -> {
            this.id = id;
            this.payer = payer;
            this.payee = payee;
            this.amount = amount;
            this.isAuthorized = isAuthorized;
        });

        payer.updateBalance(payer.getBalance() - amount);
        payee.updateBalance(payee.getBalance() + amount);
    }

    @Override
    protected void EnsureValidState() {
        if (payer.getUserType() == UserType.Merchant)
            throw new InvalidEntityStateException(
                    "Merchants can only receive funds");

        if (amount < 0)
            throw new InvalidEntityStateException(
                    "Cannot transfer negative amounts");

        if (!(payer.getBalance() >= amount))
            throw new InvalidEntityStateException(
                    "Payer doesn't have enough funds");

        if (!isAuthorized)
            throw new InvalidEntityStateException(
                    "Transaction not authorized by broker");
    }

    public UserInfo getPayer() {
        return payer;
    }

    public UserInfo getPayee() {
        return payee;
    }

    public double getAmount() {
        return amount;
    }
}
