package com.github.hbq969.code.zkc.model;

import com.github.hbq969.code.common.utils.FormatTime;
import lombok.Data;

/**
 * @author hbq
 */
@Data
public class HistoryOperate {

  private String user;
  private String operate;
  private long opTime;

  public HistoryOperate(String user, String operate, long opTime) {
    this.user = user;
    this.operate = operate;
    this.opTime = opTime;
  }

  public HistoryOperate(String user, String operate) {
    this.user = user;
    this.operate = operate;
    this.opTime = FormatTime.nowSecs();
  }

  public String getFmtOpTime() {
    return FormatTime.YYYYMMDDHHMISS.withSecs(opTime);
  }
}
