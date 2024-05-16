package com.github.hbq969.code.zkc.feign.interceptors;

import feign.auth.BasicAuthRequestInterceptor;
import java.nio.charset.Charset;

/**
 * @author : hbq969@gmail.com
 * @description : Basic认证拦截器
 * @createTime : 2023/8/26 20:20
 */
public class MyBasicAuthRequestInterceptor extends BasicAuthRequestInterceptor {

  public MyBasicAuthRequestInterceptor() {
    super("admin", "123321", Charset.forName("utf-8"));
  }
}
