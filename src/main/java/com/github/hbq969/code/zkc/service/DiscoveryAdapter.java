package com.github.hbq969.code.zkc.service;

import java.util.List;
import java.util.Map;

/**
 * @author hbq
 */
public interface DiscoveryAdapter {

  List<Map> queryAppInfos(Map map);

  void refreshConfig(Map map);
}
