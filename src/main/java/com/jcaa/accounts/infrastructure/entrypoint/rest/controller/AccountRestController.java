package com.jcaa.accounts.infrastructure.entrypoint.rest.controller;

import com.jcaa.accounts.application.port.in.CreateAccountUseCase;
import com.jcaa.accounts.application.port.in.GetAllAccountsUseCase;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.infrastructure.entrypoint.rest.dto.request.CreateAccountRestRequest;
import com.jcaa.accounts.infrastructure.entrypoint.rest.dto.response.AccountRestResponse;
import com.jcaa.accounts.infrastructure.entrypoint.rest.mapper.AccountRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Cuentas de los usuarios nuevos")
public class AccountRestController {

  private final CreateAccountUseCase createAccountUseCase;
  private final GetAllAccountsUseCase getAllAccountsUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Crear la cuenta de un usuario",
      description = "Lo invoca el microservicio users cuando se crea un usuario nuevo.")
  public AccountRestResponse create(@Valid @RequestBody final CreateAccountRestRequest request) {
    final AccountModel account =
        createAccountUseCase.execute(AccountRestMapper.toCreateCommand(request));
    return AccountRestMapper.toResponse(account);
  }

  @GetMapping
  @Operation(summary = "Listar todas las cuentas")
  public List<AccountRestResponse> getAll() {
    return AccountRestMapper.toResponseList(getAllAccountsUseCase.execute());
  }
}
