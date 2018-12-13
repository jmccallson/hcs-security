package org.hcs.ws.v1.controllers;

import ch.qos.logback.classic.Level;
import com.webcohesion.enunciate.metadata.rs.ResponseCode;
import com.webcohesion.enunciate.metadata.rs.StatusCodes;
import org.hcs.ws.v1.utils.LogbackLeveler;
import org.hcs.ws.v1.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;

/**
 * Created by Michael Buck
 * 15-Aug-2018
 */

@RestController
@RequestMapping(value="/logger")
@InternalController
public class LogControllers {

  private static final Logger LOG = LoggerFactory.getLogger(LogControllers.class);

  @StatusCodes({
    @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = SecurityUtil.NO_CONTENT_CONDITION_MSG),
    @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = SecurityUtil.INVALID_LEVEL_CONDITION_MSG),
    @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = SecurityUtil.UNAUTHORIZED_CONDITION_MSG),
    @ResponseCode(code = HttpURLConnection.HTTP_INTERNAL_ERROR, condition = SecurityUtil.INTERNAL_ERR_CONDITION_MSG)
  })
  @RequestMapping(value = "/log-level/{loggerName}/{newLevel}", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void setClassLogLevel(@PathVariable("loggerName") final String loggerName,
                               @PathVariable("newLevel") final String newLevel) {

    Level logLevel = Level.valueOf(newLevel.toUpperCase());
    String fullClassName = getFullClassName(loggerName);
    if (LogbackLeveler.changeLevel(fullClassName, logLevel)) {
      LOG.info("Log level for class '{}' successfully set to: {}", loggerName, newLevel);
    }
  }

  @StatusCodes({
    @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = SecurityUtil.INVALID_LEVEL_CONDITION_MSG),
    @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = SecurityUtil.UNAUTHORIZED_CONDITION_MSG),
    @ResponseCode(code = HttpURLConnection.HTTP_INTERNAL_ERROR, condition = SecurityUtil.INTERNAL_ERR_CONDITION_MSG)
  })
  @RequestMapping(value = "/log-level/{loggerName}", method = RequestMethod.GET)
  public String getCurrentClassLogLevel(@PathVariable("loggerName") final String loggerName) {
    String fullClassName = getFullClassName(loggerName);
    Level level = LogbackLeveler.getCurrentLogLevel(fullClassName);

    // If call didn't thrown an exception, then it worked!
    LOG.info("Log level for class '{}' is: {}", loggerName, level);
    return level.toString();
  }

  private String getFullClassName(String className) {
    String possiblePackageName = null;
    final Package[] packages = Package.getPackages();

    for (final Package pack : packages) {
      final String packageStr = pack.getName();
      possiblePackageName = packageStr + "." + className;
      try {
        Class.forName(possiblePackageName);
        break;
      } catch (final ClassNotFoundException ignored) { //NOSONAR
        // Need to keep looking
      }
    }
    if (possiblePackageName != null && !possiblePackageName.contains("familysearch")) {
      LOG.info("Possible non-familysearch class with duplicate name: {}", possiblePackageName);
    }
    return possiblePackageName;
  }
}
