package com.multicampus.gamesungcoding.a11ymarketserver.common.advice;

import com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model.ErrorRespStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model.RestErrorResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginErrResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new RestErrorResponse(
                        HttpStatus.NOT_FOUND,
                        ErrorRespStatus.USER_NOT_FOUND,
                        ex.getMessage())
                );
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestErrorResponse> handleDataNotFoundException(DataNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new RestErrorResponse(
                        HttpStatus.NOT_FOUND,
                        ErrorRespStatus.DATA_NOT_FOUND,
                        ex.getMessage())
                );
    }

    @ExceptionHandler(DataDuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<RestErrorResponse> handleDuplicatedDataException(DataDuplicatedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new RestErrorResponse(
                        HttpStatus.CONFLICT,
                        ErrorRespStatus.DUPLICATED_DATA,
                        ex.getMessage())
                );
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ErrorRespStatus.INVALID_REQUEST,
                        ex.getMessage())
                );
    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<LoginErrResponse> handleLoginFailedException(LoginFailedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new LoginErrResponse(
                        ex.getMessage())
                );
    }
}
