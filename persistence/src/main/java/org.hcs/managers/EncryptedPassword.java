package org.hcs.managers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptedPassword {
  // Encrypted Password with BCryptPasswordEncoder
  public static String encryptedPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }
}
