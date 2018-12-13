package org.hcs.ws.v1;

import org.hcs.exceptions.SecurityExpiredSessionException;
import org.hcs.exceptions.SecurityUnauthorizationException;
import org.hcs.managers.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionHandler {
  private static final String SERVER_ERROR = "server error";
  public static ResponseEntity<String> handleExceptions(Exception exception,
                                                        HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse){
    HttpStatus httpStatus;
    String error = SERVER_ERROR;

    if((exception instanceof SecurityUnauthorizationException) ||
       (exception instanceof SecurityExpiredSessionException)){
      httpStatus = HttpStatus.UNAUTHORIZED;
      error = exception.getMessage();
    } else {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    String errorMessage = StringUtils.stringNotEmpty(error) ? getErrorMessage(error, exception.getMessage()) : "";
    return new ResponseEntity<>(errorMessage, httpStatus);
  }

  private static String getErrorMessage(String error, String message){
    return "{\"error\":\"" + error + "\", \"error_description\":\"" + message + "\"}";
  }
}
