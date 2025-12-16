package org.tzi.use.api_security.exceptions;

import java.io.IOException;
import java.time.Instant;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExceptionResponse extends RuntimeException {

    public record ExceptionResponseRecord(Instant timestamp, String title, int code, Object message, String details) {
    }

    private final Instant timestamp;
    private final String title;
    private final int code;
    private final Object message;
    private final String details;

    public ExceptionResponse(Instant timestamp, String title, int code, Object message, String details) {
        this.timestamp = timestamp;
        this.title = title;
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public ExceptionResponse(HttpStatus httpStatus, Object message, String details) {
        this.timestamp = Instant.now();
        this.title = httpStatus.getReasonPhrase();
        this.code = httpStatus.value();
        this.message = message;
        this.details = details;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(this.code);
    }

    public void writeToResponse(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setStatus(this.code);

        response.setHeader("content-type", "application/problem+json");

        response.getWriter().write(String.format(
                "{\"timestamp\": \"%s\", \"title\": \"%s\", \"code\": %d, \"message\":\"%s\", \"details\": \"%s\"}",
                this.timestamp, this.title, this.code,
                this.message, this.details));

        response.getWriter().flush();
    }

    public ExceptionResponseRecord toExceptionResponseRecord() {
        return new ExceptionResponseRecord(this.timestamp, this.title, this.code, this.message, this.details);
    }

}