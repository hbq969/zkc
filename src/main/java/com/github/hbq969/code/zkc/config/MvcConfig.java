package com.github.hbq969.code.zkc.config;

import java.net.InetAddress;
import java.net.NetworkInterface;

import com.github.hbq969.code.common.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author hbq
 */
@Configuration
@Slf4j
public class MvcConfig extends WebMvcConfigurationSupport {

  private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
      "classpath:/META-INF/resources/", "classpath:/resources/",
      "classpath:/static/", "classpath:/public/"};

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!registry.hasMappingForPattern("/swagger-ui/**")) {
      registry.addResourceHandler("/swagger-ui/**").addResourceLocations(
          "classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
    if (!registry.hasMappingForPattern("/**")) {
      registry.addResourceHandler("/**").addResourceLocations(
          CLASSPATH_RESOURCE_LOCATIONS);
    }
  }

  public static void main(String[] args) throws Exception {
    InetAddress      ip      = InetAddress.getLocalHost();
    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
    byte[]           mb      = network.getHardwareAddress();
    String           mac     = StrUtils.byteArrToHex(mb);
    log.info("机器mac: {}, 机器编号: {}, 最大机器数: {}", mac);
  }
}
