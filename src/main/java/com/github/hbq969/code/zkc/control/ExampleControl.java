package com.github.hbq969.code.zkc.control;

import com.github.hbq969.code.common.cache.Expire;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.SqlUtils;
import com.github.hbq969.code.zkc.feign.DemoService;
import com.github.hbq969.code.zkc.service.ExampleService;
import com.github.hbq969.code.zkc.view.request.ExampleRequest;
import com.github.hbq969.code.zkc.view.response.ExampleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author : hbq969@gmail.com
 * @description : 示例Control类
 * @createTime : 2023/8/11 09:49
 */
@RestController
@Api(tags = "示例接口")
@Slf4j
@RequestMapping(path = "/example")
public class ExampleControl implements InitializingBean {

    @Autowired
    private ExampleService service;

    @Autowired
    private DemoService demoService;

    @Autowired
    private SpringContext context;

    @ApiOperation("查询列表")
    @RequestMapping(path = "/dict/list", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<?> queryList(
            @RequestParam(name = "key", defaultValue = "default") String key) {
        log.info("查询列表");
        return ReturnMessage.success(service.queryList(key));
    }

    @Cacheable(keyGenerator = "apiKeyGenerator", value = "default", unless = "#result.state.value!='OK'")
    @Expire(methodKey = "testCache", time = 5, unit = TimeUnit.SECONDS)
    @ApiOperation("测试接口缓存")
    @RequestMapping(path = "/cache/test", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<ExampleResponse> testCache(@RequestBody ExampleRequest request) {
        log.info("测试接口缓存: {}", request);
        return ReturnMessage.success(new ExampleResponse());
    }

    @ApiOperation("feign测试百度查询")
    @RequestMapping(path = "/queryBaidu", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<?> queryBaidu() {
        log.info("百度查询");
        return ReturnMessage.success(demoService.query());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SqlUtils.initDataSql(context.getBean(JdbcTemplate.class), "/", "test.sql", null, Charset.defaultCharset());
    }
}
