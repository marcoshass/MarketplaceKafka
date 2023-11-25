package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import org.apache.commons.lang3.StringUtils;

public class Password {
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
}
