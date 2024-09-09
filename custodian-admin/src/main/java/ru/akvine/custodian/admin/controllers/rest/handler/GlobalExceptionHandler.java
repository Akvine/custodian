package ru.akvine.custodian.admin.controllers.rest.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.akvine.common.ErrorResponse;
import ru.akvine.custodian.core.exceptions.CommonErrorCodes;
import ru.akvine.custodian.core.exceptions.app.AppAlreadyExistsException;
import ru.akvine.custodian.core.exceptions.app.AppNotFoundException;
import ru.akvine.custodian.core.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.custodian.core.exceptions.client.ClientNotFoundException;
import ru.akvine.custodian.core.exceptions.property.PropertyAlreadyExistsException;
import ru.akvine.custodian.core.exceptions.property.PropertyNotFoundException;
import ru.akvine.custodian.core.exceptions.validation.ValidationException;

import static ru.akvine.custodian.core.exceptions.CommonErrorCodes.GENERAL_ERROR;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        logger.error("Some error was occurred, exception = ", exception);
        ErrorResponse errorResponse = new ErrorResponse(GENERAL_ERROR, exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception) {
        logger.info("Validation exception, message = {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(exception.getCode(), exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ClientAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleClientAlreadyExistsException(ClientAlreadyExistsException exception) {
        logger.info("Client already exists exception, message = {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CommonErrorCodes.CLIENT_ALREADY_EXISTS_ERROR, exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ClientNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException exception) {
        logger.info("Client not found exception, message = {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CommonErrorCodes.CLIENT_NOT_FOUND_ERROR, exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleAppAlreadyExistsException(AppAlreadyExistsException exception) {
        logger.info("App already exists exception, message = {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CommonErrorCodes.APP_ALREADY_EXISTS_ERROR, exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAppNotFoundException(AppNotFoundException exception) {
        logger.info("App not found exception, message = {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CommonErrorCodes.APP_NOT_FOUND_ERROR, exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PropertyAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handlePropertyAlreadyExistsException(PropertyAlreadyExistsException exception) {
        logger.info("Property already exists exception, message = {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CommonErrorCodes.PROPERTY_ALREADY_EXISTS_ERROR, exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PropertyNotFoundException.class})
    public ResponseEntity<ErrorResponse> handlePropertyNotFoundException(PropertyNotFoundException exception) {
        logger.info("Property not found exception, message = {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CommonErrorCodes.PROPERTY_NOT_FOUND_ERROR, exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
