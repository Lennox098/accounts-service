package com.jcaa.accounts.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.jcaa.accounts.application.port.out.GetAccountByUserIdPort;
import com.jcaa.accounts.application.port.out.SaveAccountPort;
import com.jcaa.accounts.application.service.dto.command.CreateAccountCommand;
import com.jcaa.accounts.domain.enums.AccountStatus;
import com.jcaa.accounts.domain.exception.AccountAlreadyExistsException;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.domain.valueobject.AccountEmail;
import com.jcaa.accounts.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CreateAccountService")
@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

  @Mock private SaveAccountPort saveAccountPort;
  @Mock private GetAccountByUserIdPort getAccountByUserIdPort;
  @Mock private AccountNotificationService accountNotificationService;

  private CreateAccountService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new CreateAccountService(
              saveAccountPort,
              getAccountByUserIdPort,
              accountNotificationService,
              validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() guarda una cuenta ACTIVE y publica la notificación de cuenta creada")
  void shouldSaveActiveAccountAndNotify() {
    // Arrange
    final CreateAccountCommand command =
        new CreateAccountCommand("u-01", "Ana@Gmail.com", "Ana Pérez");
    when(getAccountByUserIdPort.getByUserId(any())).thenReturn(Optional.empty());
    when(saveAccountPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    final AccountModel result = service.execute(command);

    // Assert
    final ArgumentCaptor<AccountModel> captor = ArgumentCaptor.forClass(AccountModel.class);
    verify(saveAccountPort).save(captor.capture());
    final AccountModel saved = captor.getValue();
    assertAll(
        "cuenta guardada",
        () -> assertSame(saved, result),
        () -> assertEquals("u-01", saved.getUserId().value()),
        () -> assertEquals("ana@gmail.com", saved.getEmail().value()),
        () -> assertEquals(AccountStatus.ACTIVE, saved.getStatus()));
    verify(accountNotificationService).notifyAccountCreated(saved, "Ana Pérez");
  }

  @Test
  @DisplayName("execute() lanza AccountAlreadyExistsException si el usuario ya tiene cuenta")
  void shouldThrowWhenUserAlreadyHasAccount() {
    // Arrange
    final CreateAccountCommand command =
        new CreateAccountCommand("u-02", "ana@gmail.com", "Ana Pérez");
    final AccountModel existing =
        AccountModel.create(new UserId("u-02"), new AccountEmail("ana@gmail.com"));
    when(getAccountByUserIdPort.getByUserId(any())).thenReturn(Optional.of(existing));

    // Act & Assert
    assertThrows(AccountAlreadyExistsException.class, () -> service.execute(command));
    verify(saveAccountPort, never()).save(any());
    verify(accountNotificationService, never()).notifyAccountCreated(any(), any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el cuerpo es inválido")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange
    final CreateAccountCommand command = new CreateAccountCommand("", "no-es-email", "");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(saveAccountPort, getAccountByUserIdPort, accountNotificationService);
  }
}
