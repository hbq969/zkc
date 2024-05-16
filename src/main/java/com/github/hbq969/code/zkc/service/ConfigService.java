package com.github.hbq969.code.zkc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.hbq969.code.common.spring.context.UserInfo;
import com.github.hbq969.code.zkc.model.Backup;
import com.github.hbq969.code.zkc.model.HistoryOperate;
import com.github.hbq969.code.zkc.model.LeafBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hbq
 */
public interface ConfigService {

  Map queryNodes(UserInfo ui, Map map);

  void setPropertyValue(UserInfo ui, Map map);

  void deleteLeaves(UserInfo ui, Map map);

  void createFolder(UserInfo ui, Map map);

  void createNode(UserInfo ui, Map map);

  void delete(UserInfo ui, Map map);

  Set<LeafBean> exportTree(UserInfo ui, Map map);

  void importData(UserInfo ui, MultipartFile file, boolean overwrite);

  void propFileImport(MultipartFile bootstrapFile, MultipartFile defaultFile,
      MultipartFile profilesFile, boolean overwrite);

  void yamlFileImport(MultipartFile bootstrapFile, MultipartFile defaultFile,
      MultipartFile profilesFile, boolean overwrite);

  Set<LeafBean> searchTree(UserInfo ui, Map map);

  void cleanHistoryOperate();

  List<HistoryOperate> queryHistoryOperates(Map map);

  void backup();

  void recovery(Map map);

  List<Backup> queryBackups(Map map);

  @Transactional(rollbackFor = Exception.class)
  void deleteBackupById(Map map);

  @Transactional(rollbackFor = Exception.class)
  void cleanBackups();
}
