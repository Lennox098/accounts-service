package com.jcaa.accounts.domain.valueobject;

import com.jcaa.accounts.domain.exception.InvalidAccountIdException;
import java.util.Objects;
import java.util.UUID;

public record AccountId(String value) {

  private static final String FIELD_NAME = "accountId";
  private static final int MAX_LENGTH = 36;

  public AccountId {
    final String normalizedValue =
        Objects.requireNonNull(value, "AccountId cannot be null").trim();
    if (normalizedValue.isEmpty()) {
      throw InvalidAccountIdException.becauseValueIsEmpty(FIELD_NAME);
    }
    if (normalizedValue.length() > MAX_LENGTH) {
      throw InvalidAccountIdException.becauseValueIsTooLong(FIELD_NAME, MAX_LENGTH);
    }
    value = normalizedValue;
  }

  public static AccountId generate() {
    return new AccountId(UUID.randomUUID().toString());
  }

  @Override
  public String toString() {
    return value;
  }
}
