package com.github.hbq969.code.zkc.service.impl;

import java.util.List;
import java.util.Map;

import com.github.hbq969.code.zkc.service.DiscoveryAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hbq
 */
//@ConditionalOnBean({ZookeeperDiscoveryClient.class})
//@Component("agent-service-ZookeeperDiscoveryImpl")
@Slf4j
public class ZookeeperDiscoveryImpl implements DiscoveryAdapter {

  @Override
  public List<Map> queryAppInfos(Map map) {
    return null;
  }

  @Override
  public void refreshConfig(Map map) {

  }
}
