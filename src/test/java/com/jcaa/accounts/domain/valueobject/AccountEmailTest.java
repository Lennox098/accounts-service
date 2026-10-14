package com.jcaa.accounts.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.accounts.domain.exception.InvalidAccountEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("AccountEmail")
class AccountEmailTest {

  @Test
  @DisplayName("normaliza el email a minúsculas y sin espacios")
  void shouldNormalizeEmail() {
    // Arrange
    final String raw = "  Ana@Gmail.COM ";

    // Act
    final AccountEmail email = new AccountEmail(raw);

    // Assert
    assertEquals("ana@gmail.com", email.value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "sin-arroba", "ana@", "@gmail.com"})
  @DisplayName("rechaza emails vacíos o con formato inválido")
  void shouldRejectInvalidEmails(final String invalid) {
    // Arrange — valor parametrizado

    // Act & Assert
    assertThrows(InvalidAccountEmailException.class, () -> new AccountEmail(invalid));
  }
}
