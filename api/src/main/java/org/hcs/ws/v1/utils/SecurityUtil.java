package org.hcs.ws.v1.utils;

import java.util.Arrays;
import java.util.List;

public class SecurityUtil {
  public static final String OK_CONDITION_MSG = "Success";
  public static final String NO_CONTENT_CONDITION_MSG = OK_CONDITION_MSG + " (no content)";
  public static final String INVALID_RQST_CONDITION_MSG = "Bad request - Invalid JWT, JWT with missing required data, missing or invalid request header";
  public static final String INVALID_LEVEL_CONDITION_MSG = "Bad request - invalid log-level";
  public static final String UNAUTHORIZED_CONDITION_MSG = "Unauthorized - Unknown Issuer, JWT signature verification failure, or JWT has expired";
  public static final String USER_NOT_FOUND_CONDITION_MSG = "User not found - Subject in JWT cannot be found";
  public static final String INTERNAL_ERR_CONDITION_MSG = "Internal server error (unexpected error)";

  private static final String AUTHORIZATION_BEARER = "Bearer";

  @SuppressWarnings("squid:S134")
  public String getBearerToken(String adminAccessToken) {
    String adminSessionId = null;
    if (stringNotEmpty(adminAccessToken)) {
      List<String> tokens = Arrays.asList(adminAccessToken.split("\\s+"));
      if (!tokens.isEmpty()) {
        for (int indx = 0; indx < tokens.size(); indx++) {
          if (AUTHORIZATION_BEARER.equalsIgnoreCase(tokens.get(indx))) {
            adminSessionId = tokens.get(indx + 1);
            break;
          }
        }
      }
    }
    return adminSessionId;
  }

  private boolean stringNotEmpty(String params) {
    return params != null && !params.trim().isEmpty();
  }

}
