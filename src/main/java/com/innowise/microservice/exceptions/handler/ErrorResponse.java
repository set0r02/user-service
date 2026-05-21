package com.innowise.microservice.exceptions.handler;

import java.time.Instant;

public record ErrorResponse(int status, String message, Instant time) {}
