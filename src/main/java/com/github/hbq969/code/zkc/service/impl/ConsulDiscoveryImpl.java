package com.github.hbq969.code.zkc.service.impl;

import cn.hutool.core.lang.Assert;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.github.hbq969.code.common.utils.GsonUtils;
import com.github.hbq969.code.common.utils.StrUtils;
import com.github.hbq969.code.zkc.service.DiscoveryAdapter;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.stereotype.Component;

/**
 * @author hbq
 */
@ConditionalOnConsulEnabled
@Component("agent-service-ConsulDiscoveryImpl")
@Slf4j
public class ConsulDiscoveryImpl implements DiscoveryAdapter {

  @Autowired
  private ConsulDiscoveryProperties properties;

  @Autowired
  private ConsulClient client;

  @Value("${management.endpoints.web.base-path:/hbq-actuator}")
  private String defaultActuatorPath;

  private OkHttpClient http = new OkHttpClient.Builder()
      .connectTimeout(5, TimeUnit.SECONDS)
      .readTimeout(10, TimeUnit.SECONDS)
      .writeTimeout(10, TimeUnit.SECONDS)
      .connectionPool(new ConnectionPool(20, 30, TimeUnit.MINUTES))
      .build();

  @Override
  public List<Map> queryAppInfos(Map map) {
    Assert.notNull(client, "请启用服务发现");
    String serviceName = MapUtils.getString(map, "name");
    QueryParams queryParams = new QueryParams(this.properties.getConsistencyMode());
    Response<Map<String, List<String>>> res = client.getCatalogServices(queryParams, properties.getAclToken());
    if (log.isDebugEnabled()) {
      log.debug("查询到应用列表: {}", res.getValue());
    }
    List<Map> appInfos = new ArrayList<>(res.getValue().size());
    res.getValue().entrySet().forEach(e -> {
      if (StrUtils.strNotEmpty(serviceName)
          && !StringUtils.contains(e.getKey(), serviceName)) {
        return;
      }
      if ("consul".equals(e.getKey())) {
        return;
      }
      Map app = new HashMap();
      app.put("name", e.getKey());
      app.put("desc", e.getKey());
      app.put("instSize", e.getValue().size());
      appInfos.add(app);
    });
    return appInfos;
  }

  @Override
  public void refreshConfig(Map map) {
    Assert.notNull(client, "请启用服务发现");
    String serviceName = MapUtils.getString(map, "name");
    QueryParams queryParams = new QueryParams(this.properties.getConsistencyMode());
    Response<List<CatalogService>> res = client.getCatalogService(serviceName, queryParams, properties.getAclToken());
    res.getValue().forEach(service -> {
      log.info("查询到应用: {}, 的实例信息: {}", serviceName, GsonUtils.toJson(service));
      String host = Joiner.on(":").join(service.getAddress(), service.getServicePort());
      String schema = "http://";
      String actuatorPath = getServiceActuatorPath(service);
      String url = String.join("", schema, host, actuatorPath, "/refresh");
      RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), "{}");
      Callback callback = new HttpCallback(serviceName, service.getAddress(), service.getServicePort());
      log.info("刷新应用: {}, 实例: {}", serviceName, url);
      http.newCall(new Request.Builder().url(url).post(body).build()).enqueue(callback);
    });
  }

  private String getServiceActuatorPath(CatalogService service) {
    String path = defaultActuatorPath;
    List<String> tags = service.getServiceTags();
    if (CollectionUtils.isNotEmpty(tags)) {
      for (String tag : tags) {
        if (tag.contains("actuator-path")) {
          String[] array = tag.split("=");
          if (array.length == 2) {
            path = array[1];
          }
        }
      }
    }
    return path;
  }
}
