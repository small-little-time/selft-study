package com.time.core.mybatis.verify;

import com.time.core.mybatis.SQLStrategyForMybatis;
import com.time.core.mybatis.strategy.ForceIndexStrategy;
import com.time.core.mybatis.strategy.SQLStrategy;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/24 17:18
 */
public class ForceIndexVerifyHandle implements VerifyHandle {

    private static final Logger log = LoggerFactory.getLogger(ForceIndexVerifyHandle.class);

    @Override
    public SQLStrategy verify(Invocation invocation, BoundSql boundSql) {
        Connection connection = null;
        try {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            String url = "";
            Configuration configuration = mappedStatement.getConfiguration();

            if (configuration != null && configuration.getEnvironment() != null) {
                connection = configuration.getEnvironment().getDataSource().getConnection();
                url = connection.getMetaData().getURL();
            }
            for (String s : SQLStrategyForMybatis.Config.getCheckUrlList()) {
                if (url.contains(s)) {

                    return new ForceIndexStrategy();
                }
            }
        } catch (SQLException e) {
            log.error("ForceIndexVerifyHandle get Database Connection Exception: ", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("ForceIndexVerifyHandle Cloud not close Database Connection Exception: ", e);
            }
        }
        return null;
    }
}
