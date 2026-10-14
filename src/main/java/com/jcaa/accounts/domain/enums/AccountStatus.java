package com.jcaa.accounts.domain.enums;

import com.jcaa.accounts.domain.exception.InvalidAccountStatusException;
import java.util.Arrays;
import java.util.Objects;

public enum AccountStatus {
  ACTIVE,
  INACTIVE;

  public static AccountStatus fromString(final String value) {
    Objects.requireNonNull(value, "AccountStatus cannot be null");
    return Arrays.stream(values())
        .filter(status -> status.name().equalsIgnoreCase(value.trim()))
        .findFirst()
        .orElseThrow(() -> InvalidAccountStatusException.becauseValueIsInvalid(value));
  }
}
