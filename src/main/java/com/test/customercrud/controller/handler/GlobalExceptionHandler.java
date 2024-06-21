package com.test.customercrud.controller.handler;

import com.test.customercrud.dto.ErrorDto;
import com.test.customercrud.exception.CustomerNotFoundException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice("com.test.customercrud")
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCustomerNotFoundException(CustomerNotFoundException e) {
        String errorId = buildErrorId();
        log.error("Customer not found, id: {}, message: {}", errorId, e.getMessage(), e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), errorId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException e) {
        String errorId = buildErrorId();
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation error, id: {}, message: {}", errorId, errorMessage, e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, errorId);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception e) {
        String errorId = buildErrorId();
        log.error("Internal server error, id: {}, message: {}", errorId, e.getMessage(), e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", errorId);
    }

    private ResponseEntity<ErrorDto> buildErrorResponse(HttpStatus status, String message, String errorId) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setId(errorId);
        errorDto.setMessage(message);

        return ResponseEntity
                .status(status)
                .body(errorDto);
    }

    private String buildErrorId() {
        return UUID.randomUUID().toString();
    }
}
