package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordTests {

    @ParameterizedTest
    @ValueSource(strings = {""})
    public void password_With_Empty_Value_Throws_Exception(String password) {
        assertThrows(InvalidEntityStateException.class,
                () -> new Password(password));
    }

    @Test
    public void password_Exceeding_Max_Length_Throws_Exception() {
        var password = new String(new char[Password.MAX_LENGTH+1]).replace("\0", "X");
        assertThrows(InvalidEntityStateException.class,
                () -> new Password(password));
    }
}
