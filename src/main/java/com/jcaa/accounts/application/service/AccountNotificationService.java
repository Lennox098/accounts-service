package com.jcaa.accounts.application.service;

import com.jcaa.accounts.application.port.out.NotificationPublisherPort;
import com.jcaa.accounts.domain.exception.NotificationPublishException;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.domain.model.EmailDestinationModel;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountNotificationService {

  private static final String SUBJECT_ACCOUNT_CREATED =
      "Tu cuenta fue creada — Gestión de Usuarios";
  private static final String TEMPLATE_ACCOUNT_CREATED = "/templates/account-created.html";

  private static final String TOKEN_NAME = "name";
  private static final String TOKEN_EMAIL = "email";
  private static final String TOKEN_ACCOUNT_ID = "accountId";
  private static final String TOKEN_STATUS = "status";

  private final NotificationPublisherPort notificationPublisherPort;

  public void notifyAccountCreated(final AccountModel account, final String ownerName) {
    final String body =
        renderTemplate(
            loadTemplate(TEMPLATE_ACCOUNT_CREATED),
            Map.of(
                TOKEN_NAME, ownerName,
                TOKEN_EMAIL, account.getEmail().value(),
                TOKEN_ACCOUNT_ID, account.getId().value(),
                TOKEN_STATUS, account.getStatus().name()));
    notificationPublisherPort.publish(
        new EmailDestinationModel(
            account.getEmail().value(), ownerName, SUBJECT_ACCOUNT_CREATED, body));
  }

  private String loadTemplate(final String path) {
    try (InputStream inputStream = openResourceStream(path)) {
      if (Objects.isNull(inputStream)) {
        throw NotificationPublishException.becausePublishFailed(
            new IllegalStateException("Template not found: " + path));
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException ioException) {
      throw NotificationPublishException.becausePublishFailed(ioException);
    }
  }

  InputStream openResourceStream(final String path) {
    return getClass().getResourceAsStream(path);
  }

  private static String renderTemplate(final String template, final Map<String, String> values) {
    String result = template;
    for (final Map.Entry<String, String> tokenEntry : values.entrySet()) {
      result = result.replace("{{" + tokenEntry.getKey() + "}}", tokenEntry.getValue());
    }
    return result;
  }
}
