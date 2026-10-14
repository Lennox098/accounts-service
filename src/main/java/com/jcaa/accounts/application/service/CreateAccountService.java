package com.jcaa.accounts.application.service;

import com.jcaa.accounts.application.port.in.CreateAccountUseCase;
import com.jcaa.accounts.application.port.out.GetAccountByUserIdPort;
import com.jcaa.accounts.application.port.out.SaveAccountPort;
import com.jcaa.accounts.application.service.dto.command.CreateAccountCommand;
import com.jcaa.accounts.application.service.mapper.AccountApplicationMapper;
import com.jcaa.accounts.domain.exception.AccountAlreadyExistsException;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Paso 6: guarda la cuenta del usuario nuevo en TiDB.
 * Paso 7: deja en Kafka el mensaje de "cuenta creada" para que notifiers envíe el correo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {

  private static final String LOG_CREATED =
      "[CreateAccountService] cuenta creada. accountId={} userId={}";

  private final SaveAccountPort saveAccountPort;
  private final GetAccountByUserIdPort getAccountByUserIdPort;
  private final AccountNotificationService accountNotificationService;
  private final Validator validator;

  @Override
  public AccountModel execute(final CreateAccountCommand command) {
    validateCommand(command);

    final UserId userId = new UserId(command.userId());
    ensureUserHasNoAccount(userId);

    final AccountModel accountToSave = AccountApplicationMapper.fromCreateCommandToModel(command);
    final AccountModel savedAccount = saveAccountPort.save(accountToSave);
    log.info(LOG_CREATED, savedAccount.getId().value(), userId.value());

    accountNotificationService.notifyAccountCreated(savedAccount, command.nombre());

    return savedAccount;
  }

  private void validateCommand(final CreateAccountCommand command) {
    final Set<ConstraintViolation<CreateAccountCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureUserHasNoAccount(final UserId userId) {
    getAccountByUserIdPort
        .getByUserId(userId)
        .ifPresent(
            ignored -> {
              throw AccountAlreadyExistsException.becauseUserAlreadyHasAccount(userId.value());
            });
  }
}
