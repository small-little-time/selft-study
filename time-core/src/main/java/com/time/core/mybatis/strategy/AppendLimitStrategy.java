package com.time.core.mybatis.strategy;

import com.time.core.mybatis.SQLStrategyForMybatis;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;

/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/23 17:18
 */

public class AppendLimitStrategy implements SQLStrategy {

    private static final Logger log = LoggerFactory.getLogger(AppendLimitStrategy.class);

    private void log(String format, Object... arg) {
        if (!SQLStrategyForMybatis.Config.getSilence()) {
            log.error(format, arg);
        }
    }


    @Override
    public void strategyExecute(Invocation invocation, BoundSql boundSql) {
        try {
            Object[] args = invocation.getArgs();
            if (args.length == 6) {
                Field declaredField = boundSql.getClass().getDeclaredField("sql");
                declaredField.setAccessible(true);
                String newSql = boundSql.getSql() + " limit " + SQLStrategyForMybatis.Config.getMaxLimit();
                log("fixSQL:" + newSql);
                declaredField.set(boundSql, newSql);
            } else if (args.length == 4) {
                String newSql = boundSql.getSql() + " limit " + SQLStrategyForMybatis.Config.getMaxLimit();
                log("fixSQL:" + newSql);
                resetSql2Invocation(invocation, newSql);
            }
        } catch (Exception e) {
            log.error("LimitStrategy Exception ", e);
        }
    }


    private void resetSql2Invocation(Invocation invocation, String sql) throws SQLException {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newStatement = newMappedStatement(statement, new SQLStrategyForMybatis().new BoundSqlSqlSource(boundSql));

        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newStatement;
    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
}
