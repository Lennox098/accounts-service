package com.jcaa.accounts.infrastructure.entrypoint.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Cuerpo acordado por el grupo: { "userId": "...", "email": "...", "nombre": "..." }. */
public record CreateAccountRestRequest(
    @Schema(example = "8b1f2c3d-0000-4a5b-9c8d-123456789abc") @NotBlank @Size(max = 36)
        String userId,
    @Schema(example = "ana@gmail.com") @NotBlank @Email @Size(max = 150) String email,
    @Schema(example = "Ana Pérez") @NotBlank @Size(max = 100) String nombre) {}
