package com.jcaa.accounts.application.service.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountCommand(
    @NotBlank @Size(max = 36) String userId,
    @NotBlank @Email @Size(max = 150) String email,
    @NotBlank @Size(max = 100) String nombre) {}
