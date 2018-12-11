package org.hcs.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String error = "Malformed JSON request";
    return buildResponseEntity(new SecurityError(HttpStatus.BAD_REQUEST, error, ex));
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    SecurityError securityError = new SecurityError(HttpStatus.INTERNAL_SERVER_ERROR);
    securityError.setMessage(ex.getMessage());
    securityError.setDetails(request.getDescription(false));
    return buildResponseEntity(securityError);
  }

  @ExceptionHandler(SecurityAlreadyExistsException.class)
  protected ResponseEntity<Object> handleAlreadyExistException(
    SecurityAlreadyExistsException ex) {
    SecurityError securityError = new SecurityError(NOT_FOUND);
    securityError.setMessage(ex.getMessage());
    return buildResponseEntity(securityError);
  }

  private ResponseEntity<Object> buildResponseEntity(SecurityError securityError) {
    return new ResponseEntity<>(securityError, securityError.getStatus());
  }
}
