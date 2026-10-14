package com.jcaa.accounts.domain.valueobject;

import com.jcaa.accounts.domain.exception.InvalidAccountIdException;
import java.util.Objects;

/** Identificador del usuario dueño de la cuenta (lo envía el microservicio users). */
public record UserId(String value) {

  private static final String FIELD_NAME = "userId";
  private static final int MAX_LENGTH = 36;

  public UserId {
    final String normalizedValue = Objects.requireNonNull(value, "UserId cannot be null").trim();
    if (normalizedValue.isEmpty()) {
      throw InvalidAccountIdException.becauseValueIsEmpty(FIELD_NAME);
    }
    if (normalizedValue.length() > MAX_LENGTH) {
      throw InvalidAccountIdException.becauseValueIsTooLong(FIELD_NAME, MAX_LENGTH);
    }
    value = normalizedValue;
  }

  @Override
  public String toString() {
    return value;
  }
}
