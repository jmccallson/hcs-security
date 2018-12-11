package org.hcs.entities;

import org.hcs.managers.Enabled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class SecurityUserAccountDAO {
  private String userId;
  private Timestamp lastLoginDate;
  private String password;
  private Timestamp lastFailedLoginAttempt;
  private Integer failedLoginCount;
  private Enabled enabled = Enabled.UNSET;
  private Timestamp deletedDate;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Timestamp getLastLoginDate() {
    return lastLoginDate;
  }

  public void setLastLoginDate(Timestamp lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Timestamp getLastFailedLoginAttempt() {
    return lastFailedLoginAttempt;
  }

  public void setLastFailedLoginAttempt(Timestamp lastFailedLoginAttempt) {
    this.lastFailedLoginAttempt = lastFailedLoginAttempt;
  }

  public Integer getFailedLoginCount() {
    return failedLoginCount;
  }

  public void setFailedLoginCount(Integer failedLoginCount) {
    this.failedLoginCount = failedLoginCount;
  }

  public Enabled getEnabled() {
    return enabled;
  }

  public void setEnabled(Enabled enabled) {
    this.enabled = enabled;
  }

  public Timestamp getDeletedDate() {
    return deletedDate;
  }

  public void setDeletedDate(Timestamp deletedDate) {
    this.deletedDate = deletedDate;
  }
}
