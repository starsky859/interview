package com.pwc.interview.exception;

import com.pwc.interview.dto.ErrorMessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Log4j2
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ErrorMessageDto> handleRouteException(ValidationException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorMessageDto message = new ErrorMessageDto(
                LocalDateTime.now(),
                ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessageDto> handleException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorMessageDto message = new ErrorMessageDto(
                LocalDateTime.now(),
                ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
