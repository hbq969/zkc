package com.github.hbq969.code.zkc.model;

import com.github.hbq969.code.common.utils.FormatTime;
import lombok.Data;

/**
 * @author hbq
 */
@Data
public class Backup {

  private String id;
  private long backTime;
  private int size;

  public String getFmtBackupTime() {
    return FormatTime.YYYYMMDDHHMISS.withSecs(backTime);
  }
}
