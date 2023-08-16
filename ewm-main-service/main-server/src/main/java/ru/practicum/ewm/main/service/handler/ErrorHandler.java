package ru.practicum.ewm.main.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.main.service.exception.AccessException;
import ru.practicum.ewm.main.service.exception.ConflictException;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;
import ru.practicum.ewm.main.service.exception.ValidationException;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("400 — Validation Error");
        return new ErrorResponse(ValidationException.class,
                e.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.error("400 — Missing Request Header");
        return new ErrorResponse(MissingRequestHeaderException.class,
                e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("400 — Method Argument Not Valid");
        return new ErrorResponse(MethodArgumentNotValidException.class,
                e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHibernateViolationException(ConstraintViolationException e) {
        log.error("400 — Constraint Violation");
        return new ErrorResponse(ConstraintViolationException.class,
                e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("400 — Missing Servlet Request Parameter");
        return new ErrorResponse(MissingServletRequestParameterException.class,
                e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundEntity(final EntityNotFoundException e) {
        log.error(String.format("Entity not found: ", e.getEntityName()));
        return new ErrorResponse(EntityNotFoundException.class,
                e.getEntityName() + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("500 — Произошла непредвиденная ошибка.");
        return new ErrorResponse(
                String.format("Непредвиденная ошибка: " + e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException e) {
        log.error("409 — Конфликт сущностей.");
        return new ErrorResponse(String.format("Конфликт сущностей: " + e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessException(AccessException e) {
        log.error("403 — Доступ запрещен.");
        return new ErrorResponse(
                String.format("Доступ запрещен: " + e.getMessage())
        );
    }

    private static class ErrorResponse {
        String error;
        String errorClass;

        public ErrorResponse() {
            this.error = "Error message is empty";
        }

        public ErrorResponse(String error) {
            this.error = error;
        }

        public ErrorResponse(Class<?> entityClass, String message) {
            this.errorClass = entityClass.getSimpleName();
            this.error = message;
        }

        public String getErrorClass() {
            return errorClass;
        }

        public String getError() {
            return error;
        }

    }
}
