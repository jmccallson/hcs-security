package org.hcs.exceptions;

public class SecurityAlreadyExistsException extends Exception {
  public SecurityAlreadyExistsException(String message){
    super(message);
  }

  public SecurityAlreadyExistsException(String message, Throwable throwable){
    super(message, throwable);
  }
}
