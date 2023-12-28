package com.mycompany.umsapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(resourceNotFoundException.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("Z")))
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(illegalArgumentException.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("Z")))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccountLockException.class)
    public ResponseEntity<Object> handleAccountLockException(AccountLockException accountLockException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(accountLockException.getMessage())
                .statusCode(HttpStatus.LOCKED.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("Z")))
                .httpStatus(HttpStatus.LOCKED)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.LOCKED);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<Object> handleAccountLockException(InvalidFileException invalidFileException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(invalidFileException.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingHeaderException.class)
    public ResponseEntity<Object> handleAccountLockException(MissingHeaderException missingHeaderException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(missingHeaderException.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleAccountLockException(TokenExpiredException tokenExpiredException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(tokenExpiredException.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
        System.out.println("token expired method invoked !");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleAccountLockException(InvalidTokenException invalidTokenException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(invalidTokenException.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserLockException.class)
    public ResponseEntity<Object> handleAccountLockException(UserLockException userLockException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(userLockException.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleAccountLockException(BadCredentialsException badCredentialsException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(badCredentialsException.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class )
    public ResponseEntity<Object> handleAccountLockException(AccessDeniedException accessDeniedException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(accessDeniedException.getMessage())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
