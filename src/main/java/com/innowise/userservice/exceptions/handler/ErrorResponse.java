package com.innowise.userservice.exceptions.handler;

import java.time.Instant;

public record ErrorResponse(int status, String message, Instant time) {}
