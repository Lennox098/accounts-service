package com.jcaa.accounts.infrastructure.entrypoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.accounts.application.port.in.CreateAccountUseCase;
import com.jcaa.accounts.application.port.in.GetAllAccountsUseCase;
import com.jcaa.accounts.domain.exception.AccountAlreadyExistsException;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.domain.valueobject.AccountEmail;
import com.jcaa.accounts.domain.valueobject.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountRestController.class)
@DisplayName("AccountRestController")
class AccountRestControllerTest {

  private static final String BODY =
      "{\"userId\":\"u-01\",\"email\":\"ana@gmail.com\",\"nombre\":\"Ana Pérez\"}";

  @Autowired private MockMvc mockMvc;
  @MockBean private CreateAccountUseCase createAccountUseCase;
  @MockBean private GetAllAccountsUseCase getAllAccountsUseCase;

  @Test
  @DisplayName("POST /api/accounts responde 201 con la cuenta creada")
  void shouldReturnCreatedAccount() throws Exception {
    // Arrange
    final AccountModel account =
        AccountModel.create(new UserId("u-01"), new AccountEmail("ana@gmail.com"));
    when(createAccountUseCase.execute(any())).thenReturn(account);

    // Act & Assert
    mockMvc
        .perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(BODY))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value("u-01"))
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  @DisplayName("POST /api/accounts responde 409 si el usuario ya tiene cuenta")
  void shouldReturnConflictWhenAccountExists() throws Exception {
    // Arrange
    when(createAccountUseCase.execute(any()))
        .thenThrow(AccountAlreadyExistsException.becauseUserAlreadyHasAccount("u-01"));

    // Act & Assert
    mockMvc
        .perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(BODY))
        .andExpect(status().isConflict());
  }

  @Test
  @DisplayName("POST /api/accounts responde 400 si falta el email")
  void shouldReturnBadRequestWhenBodyIsInvalid() throws Exception {
    // Arrange
    final String invalidBody = "{\"userId\":\"u-01\",\"nombre\":\"Ana\"}";

    // Act & Assert
    mockMvc
        .perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(invalidBody))
        .andExpect(status().isBadRequest());
  }
}
