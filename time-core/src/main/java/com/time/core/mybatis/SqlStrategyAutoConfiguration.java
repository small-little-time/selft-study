package com.time.core.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/30 16:29
 */
public class SqlStrategyAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SqlStrategyAutoConfiguration.class);

    @Autowired
    private SqlStrategyConfig sqlStrategyConfig;


    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    public void addPageInterceptor() {
        if ("true".equals(sqlStrategyConfig.getEnable())) {
            SQLStrategyForMybatis interceptor = new SQLStrategyForMybatis();

            SQLStrategyForMybatis.Config.setMaxLimit(sqlStrategyConfig.getMaxLimit());
            SQLStrategyForMybatis.Config.setCheckOnly(sqlStrategyConfig.getCheckOnly());
            SQLStrategyForMybatis.Config.setSilence(sqlStrategyConfig.getSilence());
            SQLStrategyForMybatis.Config.setCheckUrlList(sqlStrategyConfig.getCheckUrlList());
            log.info("Init SqlStrategyAutoConfiguration checkOnly={} maxLimit={} silence={}", sqlStrategyConfig.getCheckOnly(), sqlStrategyConfig.getMaxLimit(), sqlStrategyConfig.getSilence());
            for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
                sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
            }
        }
    }
}
