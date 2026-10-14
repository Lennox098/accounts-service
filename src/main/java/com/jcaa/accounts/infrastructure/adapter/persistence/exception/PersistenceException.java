package com.jcaa.accounts.infrastructure.adapter.persistence.exception;

public final class PersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save account with ID: '%s'.";
  private static final String MESSAGE_FIND_BY_USER = "Failed to find account for user ID: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all accounts.";
  private static final String MESSAGE_READ = "Failed to read account row.";

  private PersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static PersistenceException becauseSaveFailed(
      final String accountId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE, accountId), cause);
  }

  public static PersistenceException becauseFindByUserIdFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND_BY_USER, userId), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_ALL, cause);
  }

  public static PersistenceException becauseRowCouldNotBeRead(final Throwable cause) {
    return new PersistenceException(MESSAGE_READ, cause);
  }
}
