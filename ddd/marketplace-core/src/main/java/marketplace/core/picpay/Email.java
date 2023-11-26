package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import marketplace.shared.ValueObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class Email extends ValueObject<Email> {
    public static final int MAX_LENGTH = 255;
    private String value;

    public Email(String value) {
        if (StringUtils.isBlank(value))
            throw new InvalidEntityStateException("Email cannot be empty");

        if (!isValidEmail(value))
            throw new InvalidEntityStateException("Email must be valid");

        this.value = value;
    }

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public String getValue() {
        return value;
    }

    @Override
    protected boolean equalsCore(Object o) {
        var other = (Email) o;
        return Objects.equals(value, other.getValue());
    }

    @Override
    protected int getHashCodeCore() {
        return Objects.hash(value);
    }
}
