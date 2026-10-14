package com.jcaa.accounts.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.jcaa.accounts.application.port.out.NotificationPublisherPort;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.domain.model.EmailDestinationModel;
import com.jcaa.accounts.domain.valueobject.AccountEmail;
import com.jcaa.accounts.domain.valueobject.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("AccountNotificationService")
@ExtendWith(MockitoExtension.class)
class AccountNotificationServiceTest {

  @Mock private NotificationPublisherPort notificationPublisherPort;
  @InjectMocks private AccountNotificationService service;

  @Test
  @DisplayName("notifyAccountCreated() publica el correo de cuenta creada con la plantilla llena")
  void shouldPublishRenderedAccountCreatedEmail() {
    // Arrange
    final AccountModel account =
        AccountModel.create(new UserId("u-01"), new AccountEmail("ana@gmail.com"));

    // Act
    service.notifyAccountCreated(account, "Ana Pérez");

    // Assert
    final ArgumentCaptor<EmailDestinationModel> captor =
        ArgumentCaptor.forClass(EmailDestinationModel.class);
    verify(notificationPublisherPort).publish(captor.capture());
    final EmailDestinationModel email = captor.getValue();
    assertAll(
        "correo de cuenta creada",
        () -> assertEquals("ana@gmail.com", email.getDestinationEmail()),
        () -> assertEquals("Ana Pérez", email.getDestinationName()),
        () -> assertTrue(email.getSubject().contains("cuenta fue creada")),
        () -> assertTrue(email.getBody().contains("Ana Pérez")),
        () -> assertTrue(email.getBody().contains(account.getId().value())),
        () -> assertFalse(email.getBody().contains("{{"), "no deben quedar tokens sin reemplazar"));
  }
}
