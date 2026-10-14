package com.jcaa.accounts.domain.exception;

public final class AccountAlreadyExistsException extends DomainException {

  private static final String MESSAGE_USER_HAS_ACCOUNT =
      "El usuario con ID '%s' ya tiene una cuenta.";

  private AccountAlreadyExistsException(final String message) {
    super(message);
  }

  public static AccountAlreadyExistsException becauseUserAlreadyHasAccount(final String userId) {
    return new AccountAlreadyExistsException(String.format(MESSAGE_USER_HAS_ACCOUNT, userId));
  }
}
