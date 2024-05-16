package com.github.hbq969.code.zkc.dao.policy;

import com.github.hbq969.code.common.datasource.context.AbstractLookupKeyPolicy;
import com.github.hbq969.code.common.spring.context.SpringContext;
import lombok.AllArgsConstructor;

/**
 * @author : hbq969@gmail.com
 * @description : 使用数据库key作为策略
 * @createTime : 2023/8/11 09:47
 */
@AllArgsConstructor
public class DefaultLookupKeyPolicy extends AbstractLookupKeyPolicy {

  @Override
  protected String getDatasourceKey(SpringContext context) {
    return "hikari";
  }
}
