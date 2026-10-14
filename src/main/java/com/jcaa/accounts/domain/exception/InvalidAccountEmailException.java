package com.jcaa.accounts.domain.exception;

public final class InvalidAccountEmailException extends DomainException {

  private static final String MESSAGE_EMPTY = "El email de la cuenta no puede estar vacío.";
  private static final String MESSAGE_FORMAT = "El email de la cuenta no tiene un formato válido.";

  private InvalidAccountEmailException(final String message) {
    super(message);
  }

  public static InvalidAccountEmailException becauseValueIsEmpty() {
    return new InvalidAccountEmailException(MESSAGE_EMPTY);
  }

  public static InvalidAccountEmailException becauseFormatIsInvalid() {
    return new InvalidAccountEmailException(MESSAGE_FORMAT);
  }
}
