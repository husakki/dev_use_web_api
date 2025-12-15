package org.tzi.use.api_security.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpHeaders headers, HttpStatusCode status, WebRequest request) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, errors,
                                request.getDescription(false));

                System.out.println(exceptionResponse);

                // Using ExceptionResponseRecord here because else the whole stack trace of parent class RuntimeError is included which would give a tone of server information to the client
                return new ResponseEntity<>(exceptionResponse.toExceptionResponseRecord(),
                                exceptionResponse.getHttpStatus());
        }
}