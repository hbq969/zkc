package com.github.hbq969.code.zkc.dao;

import com.github.hbq969.code.zkc.dao.entity.DictEntity;
import com.github.hbq969.code.common.datasource.DS;
import com.github.hbq969.code.common.datasource.context.DefaultPolicy;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : hbq969@gmail.com
 * @description : 示例数据访问层接口
 * @createTime : 2023/8/11 09:05
 */
@Mapper
@DS(DefaultPolicy.class)
public interface ExampleDao {

  List<DictEntity> queryList();
}
