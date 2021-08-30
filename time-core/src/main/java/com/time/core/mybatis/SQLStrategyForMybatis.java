package com.time.core.mybatis;


import com.time.core.mybatis.strategy.SQLStrategy;
import com.time.core.mybatis.verify.VerifyHandleContext;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;


/**
 * SQL公共策略拦截器，MyBatis使用
 *
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),})
public class SQLStrategyForMybatis implements Interceptor {


    @Override
    public void setProperties(Properties arg0) {
        Config.setMaxLimit(arg0.getProperty("maxLimit"));
        Config.setCheckOnly(arg0.getProperty("checkOnly"));
        Config.setSilence(arg0.getProperty("silence"));
        Config.setCheckUrlList(arg0.getProperty("checkUrlList"));
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();

        BoundSql boundSql = null;
        if (args.length == 6) {
            boundSql = (BoundSql) args[5];
        } else if (args.length == 4) {
            MappedStatement ms = (MappedStatement) args[0];
            boundSql = ms.getBoundSql(args[1]);
        }

        VerifyHandleContext verifyHandleContext = new VerifyHandleContext();
        List<SQLStrategy> sqlStrategyList = verifyHandleContext.process(invocation, boundSql);
        for (SQLStrategy sqlStrategy : sqlStrategyList) {
            if (sqlStrategy != null) {
                sqlStrategy.strategyExecute(invocation, boundSql);
            }
        }

        return invocation.proceed();
    }


    // 定义一个内部辅助类，作用是包装sql
    public class BoundSqlSqlSource implements SqlSource {

        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }


    public static class Config {
        private static String maxLimit = "5000";

        private static boolean checkOnly = true;

        private static boolean silence = false;


        private static String[] checkUrlList = new String[]{};

        private static final Logger log = LoggerFactory.getLogger(Config.class);

        private static void log(String format, Object... arg) {
            if (!Config.getSilence()) {
                log.error(format, arg);
            }
        }

        public static String[] getCheckUrlList() {
            return checkUrlList;
        }

        public static void setCheckUrlList(String checkUrlList) {
            if (checkUrlList != null && !checkUrlList.isEmpty()) {
                Config.checkUrlList = checkUrlList.split(";");
            }
        }

        public static String getMaxLimit() {
            return maxLimit;
        }

        public static void setMaxLimit(String property) {
            if (property != null && !property.isEmpty()) {
                try {
                    Integer i = Integer.parseInt(property);
                    if (i.intValue() < 1) {
                        log("maxLimt不能小于1，已经设置为5000 ;入参值：{}", i);
                    }
                    if (i.intValue() > 5000) {
                        maxLimit = "5000";
                        log("maxLimt不能大于5000，已经设置为5000;入参值：{}", i);
                    } else {
                        Config.maxLimit = i + "";
                    }
                } catch (Exception e) {
                    log.error("maxLimt不是一个合法的数字;入参值：{}", property);
                }
            }
        }

        public static boolean getCheckOnly() {
            return checkOnly;
        }

        public static void setCheckOnly(String property) {
            if (property != null && !property.isEmpty()) {
                Config.checkOnly = "true".equals(property);
            }
        }

        public static boolean getSilence() {
            return silence;
        }

        public static void setSilence(String property) {
            if (property != null && !property.isEmpty()) {
                Config.silence = "true".equals(property);
            }
        }
    }
}
