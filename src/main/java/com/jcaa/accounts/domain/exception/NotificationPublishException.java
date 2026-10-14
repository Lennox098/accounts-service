package com.jcaa.accounts.domain.exception;

public final class NotificationPublishException extends DomainException {

  private static final String MESSAGE_FAILED = "La notificación no pudo publicarse en Kafka.";

  private NotificationPublishException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static NotificationPublishException becausePublishFailed(final Throwable cause) {
    return new NotificationPublishException(MESSAGE_FAILED, cause);
  }
}
