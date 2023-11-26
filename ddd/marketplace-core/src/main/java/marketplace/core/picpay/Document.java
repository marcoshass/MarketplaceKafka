package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import marketplace.shared.ValueObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

public class Document extends ValueObject<Document> {
    public static final int MAX_LENGTH = 50;
    private String docType;
    private String value;

    public Document(String value, UserType userType) {
        docType = userType == UserType.Regular ? "Cpf" : "Cnpj";

        if (StringUtils.isBlank(value))
            throw new InvalidEntityStateException("Document type cannot be empty");

        if (!isValid(docType, value))
            throw new InvalidEntityStateException("Document is invalid");

        this.value = value;
    }

    public String getDocType() {
        return docType;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected boolean equalsCore(Object o) {
        var other = (Document) o;
        return Objects.equals(docType, other.getDocType()) &&
                Objects.equals(value, other.getValue());
    }

    @Override
    protected int getHashCodeCore() {
        return Objects.hash(docType, value);
    }

    private boolean isValid(String docType, String docNumber) {
        return docType.equals("Cpf") ? isCpf(docNumber) : isCnpj(docNumber);
    }

    public boolean isCpf(String cpf) {
        int[] multiplicador1 = { 10, 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] multiplicador2 = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
        String tempCpf;
        String digito;
        int soma;
        int resto;
        cpf = cpf.trim();
        cpf = cpf.replace(".", "").replace("-", "");
        if (cpf.length() != 11)
            return false;
        tempCpf = cpf.substring(0, 9);
        soma = 0;

        for (int i = 0; i < 9; i++)
            soma += Integer.parseInt(String.valueOf(tempCpf.charAt(i))) * multiplicador1[i];
        resto = soma % 11;
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = String.valueOf(resto);
        tempCpf = tempCpf + digito;
        soma = 0;
        for (int i = 0; i < 10; i++)
            soma += Integer.parseInt(String.valueOf(tempCpf.charAt(i))) * multiplicador2[i];
        resto = soma % 11;
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = digito + resto;
        return cpf.endsWith(digito);
    }

    private boolean isCnpj(String cnpj) {
        int[] multiplicador1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] multiplicador2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        int soma;
        int resto;
        String digito;
        String tempCnpj;
        cnpj = cnpj.trim();
        cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "");
        if (cnpj.length() != 14)
            return false;
        tempCnpj = cnpj.substring(0, 12);
        soma = 0;
        for (int i = 0; i < 12; i++)
            soma += Integer.parseInt(String.valueOf(tempCnpj.charAt(i))) * multiplicador1[i];
        resto = (soma % 11);
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = String.valueOf(resto);
        tempCnpj = tempCnpj + digito;
        soma = 0;
        for (int i = 0; i < 13; i++)
            soma += Integer.parseInt(String.valueOf(tempCnpj.charAt(i))) * multiplicador2[i];
        resto = (soma % 11);
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = digito + resto;
        return cnpj.endsWith(digito);
    }
}
