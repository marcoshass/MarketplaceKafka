package marketplace.core.picpay;

import marketplace.core.InvalidEntityStateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTests {

    @Test
    public void document_With_Empty_Value_Throws_Exception()
    {
        assertThrows(InvalidEntityStateException.class,
                () -> new Document("", UserType.Regular));
    }

    @Test
    public void document_With_Invalid_Cpf_Throws_Exception()
    {
        assertThrows(InvalidEntityStateException.class,
                () -> new Document("27191366894", UserType.Regular));
    }

    @Test
    public void document_With_Invalid_Cnpj_Throws_Exception()
    {
        assertThrows(InvalidEntityStateException.class,
                () -> new Document("27191366894", UserType.Merchant));
    }

    @Test
    public void document_Can_Create_With_Valid_Cpf()
    {
        var document = new Document("27191366893", UserType.Regular);
        assertNotNull(document);
    }

    @Test
    public void document_Can_Create_With_Valid_Cnpj()
    {
        var document = new Document("35298117000188", UserType.Merchant);
        assertNotNull(document);
    }
}