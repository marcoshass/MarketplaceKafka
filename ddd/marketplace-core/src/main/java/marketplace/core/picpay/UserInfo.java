package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import marketplace.shared.AggregateRoot;
import org.apache.commons.lang3.StringUtils;

public class UserInfo extends AggregateRoot<Integer> {
    public static final  int FIRSTNAME_MAX_LENGTH = 255;
    public static final int LASTNAME_MAX_LENGTH = 255;

    private int id;
    private UserType userType;
    private String firstName;
    private String lastName;
    private Document document;
    private Email email;
    private Password password;
    private double balance;
    private byte[] rowVersion;

    public UserInfo(int id, UserType userType, String firstName,
                    String lastName, Document document, Email email,
                    Password password, double balance) {
        apply(o -> {
            this.id = id;
            this.userType = userType;
            this.firstName = firstName;
            this.lastName = lastName;
            this.document = document;
            this.email = email;
            this.password = password;
            this.balance = balance;
        });
    }

    @Override
    protected void EnsureValidState() {
        var valid = id > 0 &&
                !StringUtils.isBlank(firstName) &&
                !(firstName.length() > FIRSTNAME_MAX_LENGTH);

        if (!StringUtils.isBlank(lastName))
            valid &= !(lastName.length() > LASTNAME_MAX_LENGTH);

        if (!valid)
            throw new InvalidEntityStateException(
                    "Post-checks failed for entity UserInfo");
    }

    public void updateBalance(double balance) {
        apply(o -> {
            this.balance = balance;
        });
    }

    public void update(UserType userType, String firstName,
                       String lastName, Document document, Email email,
                       Password password, double balance)
    {
        apply(o -> {
            this.userType = userType;
            this.firstName = firstName;
            this.lastName = lastName;
            this.document = document;
            this.email = email;
            this.password = password;
            this.balance = balance;
        });
    }

    public UserType getUserType() {
        return userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Document getDocument() {
        return document;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public byte[] getRowVersion() {
        return rowVersion;
    }
}
