package com.meetime.hubspot.domain;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String error, String message) {
}