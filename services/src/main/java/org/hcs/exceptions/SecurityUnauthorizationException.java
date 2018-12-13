package org.hcs.exceptions;

public class SecurityUnauthorizationException extends Exception{
  public SecurityUnauthorizationException(String message, Throwable throwable){
    super(message, throwable);
  }
}
