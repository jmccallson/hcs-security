package org.hcs.ws.v1.utils;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  Provides a general purpose method to dynamically change the log level for a class SLF4J logger.
 *  This code assumes that the underlying logging implementation is: logback (ch.qos.logback)
 *
 *  This code won't work for underlying log4j or java.util.logging implementations.
 *  However, the code could easily be modified to support these implementations.
 *  (Check to determine the underlying logging implementation and provide a means
 *  to translate the SLF4J Level argument to the alternate implementation's Level
 *  type.)
 *
 */
public class LogbackLeveler {

  private static final Map<Level, Level > TO_QOS_LEVEL_MAP = createTranslateMap();

  private LogbackLeveler() {
    //hide
  }

  /**
   * Attemp to change the log level for the selected Logback logger
   *  associated with a named Java class.
   * @param loggerClassName fully qualified class name of the logger to be changed
   * @param newLevel the new logging level
   * @return true if logging level successfully changed
   */
  public static boolean changeLevel(String loggerClassName, Level newLevel) {
    Logger classLogger;
    classLogger = LoggerFactory.getLogger(loggerClassName);

    // Make sure we're dealing with a LogBack logger!
    if (!(classLogger instanceof ch.qos.logback.classic.Logger)) {
      throw new IllegalArgumentException("Invalid logger implementation: " + classLogger.getClass().getName());
    }

    ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger)classLogger;

    logbackLogger.setLevel(TO_QOS_LEVEL_MAP.get( newLevel));
    return true;
  }

  public static Level getCurrentLogLevel(String loggerClassName) {
    Logger classLogger;
    classLogger = LoggerFactory.getLogger(loggerClassName);

    // Make sure we're dealing with a LogBack logger!
    if (!(classLogger instanceof ch.qos.logback.classic.Logger)) {
      throw new IllegalArgumentException("Invalid logger implementation: " + classLogger.getClass().getName());
    }

    ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger)classLogger;
    return logbackLogger.getEffectiveLevel();
  }

  //
  // Provide a map to translate SLF4J Level to Logback (QOS) Level
  //
  private static Map<Level, Level> createTranslateMap() {
    Map<Level, Level>  qosMap = new HashMap<>();

    qosMap.put(Level.TRACE, Level.TRACE);
    qosMap.put(Level.DEBUG, Level.DEBUG);
    qosMap.put(Level.INFO, Level.INFO);
    qosMap.put(Level.WARN, Level.WARN);
    qosMap.put(Level.ERROR, Level.ERROR);

    return Collections.unmodifiableMap(qosMap);
  }
}
