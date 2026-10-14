package com.jcaa.accounts.infrastructure.entrypoint.rest.dto.response;

import java.time.LocalDateTime;

public record AccountRestResponse(
    String id, String userId, String email, String status, LocalDateTime createdAt) {}
