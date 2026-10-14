package com.jcaa.accounts.domain.valueobject;

import com.jcaa.accounts.domain.exception.InvalidAccountEmailException;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record AccountEmail(String value) {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  public AccountEmail {
    final String normalizedValue =
        Objects.requireNonNull(value, "AccountEmail cannot be null")
            .trim()
            .toLowerCase(Locale.ROOT);
    if (normalizedValue.isEmpty()) {
      throw InvalidAccountEmailException.becauseValueIsEmpty();
    }
    if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidAccountEmailException.becauseFormatIsInvalid();
    }
    value = normalizedValue;
  }

  @Override
  public String toString() {
    return value;
  }
}
