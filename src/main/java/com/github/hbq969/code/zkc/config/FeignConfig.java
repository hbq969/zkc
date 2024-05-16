package com.github.hbq969.code.zkc.config;

import com.github.hbq969.code.zkc.feign.DemoService;
import com.github.hbq969.code.zkc.feign.interceptors.MyBasicAuthRequestInterceptor;
import com.github.hbq969.code.common.spring.cloud.feign.FeignFactoryBean;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.RequestInterceptor;
import feign.okhttp.OkHttpClient;
import java.util.LinkedList;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : hbq969@gmail.com
 * @description : Feign自动转配类
 * @createTime : 2023/8/26 20:15
 */
@Configuration
@Slf4j
public class FeignConfig {

  @Value("${feign.service.example.url:https://www.baidu.com}")
  private String exampleServiceUrl;

  @Bean("feign-demoService")
  public DemoService demoService() throws Exception {
    FeignFactoryBean factory = new FeignFactoryBean() {
      @Override
      protected LinkedList<RequestInterceptor> interceptors() {
        LinkedList<RequestInterceptor> list = super.interceptors();
        list.addFirst(new MyBasicAuthRequestInterceptor());
        return list;
      }

      @Override
      protected Encoder encoder() {
      return new Encoder.Default();
      }

      @Override
      protected Decoder decoder() {
      return new Decoder.Default();
      }
    };
    factory.setInter(DemoService.class);
    factory.setUrl(exampleServiceUrl);
    return (DemoService) factory.getObject();
  }
}
