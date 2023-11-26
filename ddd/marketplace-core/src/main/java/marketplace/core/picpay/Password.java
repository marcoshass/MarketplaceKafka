package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import marketplace.shared.ValueObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

public class Password extends ValueObject<Password> {
    public static final int MAX_LENGTH = 255;
    public String value;

    public Password(String value)
    {
        if (StringUtils.isBlank(value))
            throw new InvalidEntityStateException("Password cannot be empty");

        if (value.length() > MAX_LENGTH)
            throw new InvalidEntityStateException(String.format(
                    "Password length cannot exceed %d characters", MAX_LENGTH));

        this.value = value;
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
