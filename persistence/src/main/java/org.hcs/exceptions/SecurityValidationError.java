package org.hcs.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SecurityValidationError extends SecuritySubError{
  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  SecurityValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }
}

abstract class SecuritySubError {

}