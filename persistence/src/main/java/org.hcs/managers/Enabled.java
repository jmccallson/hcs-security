package org.hcs.managers;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Enabled {
  UNSET("U"),
  ENABLED("T"),
  NOT_ENABLED("F"),
  DISABLED("D");

  private static final Map<String, Enabled> MAP = new HashMap<>();
  private String state;

  static {
    for (Enabled element : EnumSet.allOf(Enabled.class)) {
      MAP.put(element.getState(), element);
    }
  }

  Enabled(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }

  public static Enabled getByValue(String value) {
    if (null == MAP.get(value)) {
      return NOT_ENABLED;
    }
    return MAP.get(value);
  }
}
