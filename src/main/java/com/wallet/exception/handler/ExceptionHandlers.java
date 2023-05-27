package com.wallet.exception.handler;

import com.wallet.exception.AuthenticationException;
import com.wallet.exception.NotFoundException;
import com.wallet.model.dto.error.ErrorResponseDto;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(NotFoundException e) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(status.toString(), e.getMessage()));
    }

    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class,
        PSQLException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(Exception e) {
        var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponseDto(status.toString(), e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException e) {
        var status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(new ErrorResponseDto(status.toString(), e.getMessage()));
    }
}
