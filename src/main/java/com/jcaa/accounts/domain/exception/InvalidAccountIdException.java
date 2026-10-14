package com.jcaa.accounts.domain.exception;

public final class InvalidAccountIdException extends DomainException {

  private static final String MESSAGE_EMPTY = "El identificador '%s' no puede estar vacío.";
  private static final String MESSAGE_TOO_LONG =
      "El identificador '%s' no puede superar %d caracteres.";

  private InvalidAccountIdException(final String message) {
    super(message);
  }

  public static InvalidAccountIdException becauseValueIsEmpty(final String fieldName) {
    return new InvalidAccountIdException(String.format(MESSAGE_EMPTY, fieldName));
  }

  public static InvalidAccountIdException becauseValueIsTooLong(
      final String fieldName, final int maxLength) {
    return new InvalidAccountIdException(String.format(MESSAGE_TOO_LONG, fieldName, maxLength));
  }
}
