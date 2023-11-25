package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {
    private static UserType defaultUserType = UserType.Regular;
    private static String defaultFirstName = "John";
    private static String defaultLastName = "Doe";
    private static Document defaultCpf = new Document("27191366893", UserType.Regular);
    private static Document defaultCnpj = new Document("35298117000188", UserType.Merchant);
    private static Email defaultEmail = new Email("johndoe@gmail.com");
    private static Password defaultPasssword = new Password("Yukon900");
    private static double defaultBalance = 0;

    @Test
    public void regular_User_Can_Transfer_Amounts_To_Regular_User()
    {
        double amount = 100.0;

        var payer = createUser(1, UserType.Regular, 100);
        var payee = createUser(2, UserType.Regular);
        var transaction = new Transaction(1, payer, payee, amount, true);

        assertNotNull(transaction);
    }

    @Test
    public void regular_User_Can_Transfer_Amounts_To_Merchant_User()
    {
        double amount = 100.0;
        var payer = createUser(1, UserType.Regular, 100);
        var payee = createUser(2, UserType.Merchant, defaultCnpj);
        var transaction = new Transaction(1, payer, payee, amount, true);

        assertNotNull(transaction);
    }

    @Test
    public void regular_User_Cannot_Transfer_Negative_Amounts()
    {
        double amount = -100.0;
        var payer = createUser(1, UserType.Regular, 100);
        var payee = createUser(2, UserType.Regular);

        assertThrows(InvalidEntityStateException.class,
                () -> new Transaction(1, payer, payee, amount, true));
    }

    @Test
    public void merchant_Cannot_Transfer_Amounts_To_Regular_User()
    {
        double amount = 100.0;
        var payer = createUser(1, UserType.Merchant, defaultCnpj, 100);
        var payee = createUser(2, UserType.Regular);

        assertThrows(InvalidEntityStateException.class,
                () -> new Transaction(1, payer, payee, amount, true));
    }

    @Test
    public void merchant_Cannot_Transfer_Amounts_To_Another_Merchant()
    {
        double amount = 100.0;
        var payer = createUser(1, UserType.Merchant, defaultCnpj, 100);
        var payee = createUser(2, UserType.Merchant, defaultCnpj);

        assertThrows(InvalidEntityStateException.class,
                () -> new Transaction(1, payer, payee, amount, true));
    }

    public static Stream<Arguments> payer_Without_Funds_Cannot_Transfer_Amounts() {
        return Stream.of(
                Arguments.of(0, 100),
                Arguments.of(-100, 100),
                Arguments.of(0.95, 0.951)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void payer_Without_Funds_Cannot_Transfer_Amounts(double balance, double amount)
    {
        var payer = createUser(1, UserType.Regular, balance);
        var payee = createUser(2, UserType.Regular);

        assertThrows(InvalidEntityStateException.class,
                () -> new Transaction(1, payer, payee, amount, true));
    }

    @Test
    public void payer_Cannot_Transfer_Funds_When_Unauthorized()
    {
        double amount = 100.0;
        var payer = createUser(1, UserType.Regular, 100);
        var payee = createUser(2, UserType.Regular);

        assertThrows(InvalidEntityStateException.class,
                () -> new Transaction(1, payer, payee, amount, false));
    }

    @Test
    public void payer_Can_Transfer_Funds_When_Authorized()
    {
        double amount = 100.0;
        var payer = createUser(1, UserType.Regular, 100);
        var payee = createUser(2, UserType.Regular);
        var transaction = new Transaction(1, payer, payee, amount, true);

        assertNotNull(transaction);
    }

    @Test
    public void payer_Balance_Decreases_After_Sending_Funds_To_Payee()
    {
        double initialBalance = 100.0;
        double amountTransfered = 10.0;
        var payer = createUser(1, UserType.Regular, initialBalance);
        var payee = createUser(2, UserType.Regular);

        var transaction = new Transaction(1, payer, payee, amountTransfered, true);

        assertEquals (90, transaction.getPayer().getBalance());
    }

    @Test
    public void payee_Balance_Increases_After_Receiving_Funds()
    {
        double payeeBalance = 100.0;
        double amountReceived = 10.0;
        var payer = createUser(1, UserType.Regular, 1000);
        var payee = createUser(2, UserType.Regular, payeeBalance);

        var transaction = new Transaction(1, payer, payee, amountReceived, true);

        assertEquals(110, transaction.getPayee().getBalance());
    }

    @Test
    public void payer_Payee_And_Transaction_Balances_Were_Affected_By_The_Transfer()
    {
        double payerBalance = 1000.0;
        var payer = createUser(1, UserType.Regular, payerBalance);

        double payeeBalance = 0.0;
        var payee = createUser(2, UserType.Regular, payeeBalance);

        double amountTransfered = 100.0;
        var transaction = new Transaction(1, payer, payee, amountTransfered, true);

        assertEquals(900, transaction.getPayer().getBalance());
        assertEquals(100.0, transaction.getPayee().getBalance());
        assertEquals(100.0, transaction.getAmount());
    }

    // Factories

    private static UserInfo createUser(int id) {
        return new UserInfo(id, defaultUserType, defaultFirstName,
                defaultLastName, defaultCpf, defaultEmail, defaultPasssword,
                defaultBalance);
    }

    private static UserInfo createUser(int id, UserType userType) {
        return new UserInfo(id, userType, defaultFirstName,
                defaultLastName, defaultCpf, defaultEmail, defaultPasssword,
                defaultBalance);
    }

    private static UserInfo createUser(int id, UserType userType, double balance) {
        return new UserInfo(id, userType, defaultFirstName,
                defaultLastName, defaultCpf, defaultEmail, defaultPasssword,
                balance);
    }

    private static UserInfo createUser(int id, UserType userType, Document document) {
        return new UserInfo(id, userType, defaultFirstName,
                defaultLastName, document, defaultEmail, defaultPasssword,
                defaultBalance);
    }

    private static UserInfo createUser(int id, UserType userType, Document document, double balance) {
        return new UserInfo(id, userType, defaultFirstName,
                defaultLastName, document, defaultEmail, defaultPasssword,
                balance);
    }
}
