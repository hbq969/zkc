package com.github.hbq969.code.zkc.feign;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author : hbq969@gmail.com
 * @description : Feign客户端桩代码接口
 * @createTime : 2023/8/26 20:17
 */
public interface DemoService {

  @RequestMapping(path = "/s", method = RequestMethod.GET)
  String query();
}
