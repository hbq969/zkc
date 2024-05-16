package com.github.hbq969.code.zkc.dao;

import java.util.List;
import java.util.Map;

import com.github.hbq969.code.zkc.model.Backup;
import com.github.hbq969.code.zkc.model.BackupDetail;
import com.github.hbq969.code.zkc.model.HistoryOperate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

/**
 * @author hbq
 */
@Repository("config-dao-ConfigDao")
@Mapper
public interface ConfigDao {

  void createConfigHistory();

  void saveHistoryOperate(HistoryOperate history);

  void deleteHistoryOperate(@Param("opTime") long opTime);

  List<HistoryOperate> queryHistoryOperates(Map map);

  void createBackupMain();

  void createBackupDetail();

  List<Backup> queryBackups(@Param("startTime") long startTime,
                            @Param("endTime") long endTime);

  List<BackupDetail> queryBackupDetails(@Param("id") String id);

  void deleteBackupById(@Param("id") String id);

  void deleteBackupDetailById(@Param("id") String id);

  void deleteBackupByTime(@Param("backTime") long backTime);

  void deleteBackupDetailByTime(@Param("backTime") long backTime);

  void createUserInfo();

  void createUserAuthorities();
}
