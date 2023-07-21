package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.between;

@SpringBootTest
public class CardUtilsTests {
    @Test
    public void cardNumberIsCreated() { // Número de tarjeta no sea nulo o vacío
        String cardNumber = CardUtils.getCardNumber();

        assertThat(cardNumber, is(not(emptyOrNullString())));
    }
    @Test
    public void cardNumberIsUnique() { // Número de tarjeta no repetido
        String cardNumber1 = CardUtils.getCardNumber();
        String cardNumber2 = CardUtils.getCardNumber();

        assertNotEquals(cardNumber1, cardNumber2);
    }

    @Test
    public void cvvValidLength() { // Longitud válida: 3 dígitos
        short cvvNumber = CardUtils.getCvv();

        assertTrue(cvvNumber >= 100 && cvvNumber <= 999);
    }

    @Test
    public void cvvIsNotNull() {
        short cvvNumber = CardUtils.getCvv();

        assertThat(cvvNumber, is(not(nullValue())));
    }
}
