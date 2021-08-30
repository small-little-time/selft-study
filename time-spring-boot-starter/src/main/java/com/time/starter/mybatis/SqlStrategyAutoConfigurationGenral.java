package com.time.starter.mybatis;

import com.time.core.mybatis.SqlStrategyAutoConfiguration;
import com.time.core.mybatis.SqlStrategyConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/30 14:38
 */

@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@EnableConfigurationProperties(SqlStrategyConfig.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class SqlStrategyAutoConfigurationGenral extends SqlStrategyAutoConfiguration {

    @PostConstruct
    public void init() {
        super.addPageInterceptor();
    }

}
