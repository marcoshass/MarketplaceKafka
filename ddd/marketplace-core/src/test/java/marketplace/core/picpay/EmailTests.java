package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTests {

    @ParameterizedTest
    @ValueSource(strings = {"", "john_doe@@gmail.com"})
    public void email_With_Invalid_Value_Throws_Exception(String email) {
        assertThrows(InvalidEntityStateException.class,
                () -> new Email(email));
    }
}
