package com.time.core.mybatis.strategy;

import com.time.core.mybatis.SQLStrategyForMybatis;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/24 19:14
 */
public class ForceIndexStrategy implements SQLStrategy {

    private static final Logger log = LoggerFactory.getLogger(ForceIndexStrategy.class);

    private void log(String format, Object... arg) {
        if (!SQLStrategyForMybatis.Config.getSilence()) {
            log.error(format, arg);
        }
    }


    @Override
    public void strategyExecute(Invocation invocation, BoundSql boundSql) {
        String sql = boundSql.getSql().toLowerCase();
        String lowerCase = sql.toLowerCase().replaceAll("\t", " ").replaceAll("\n", " ").replaceAll("\r", " ");
        boolean contains = lowerCase.substring(lowerCase.indexOf("from")).contains("force index");
        if (!contains) {
            log("查询TIDB的SQL语句未指定索引，SQL={}", boundSql.getSql().replaceAll("\t", " ").replaceAll("\n", " "));
            throw new RuntimeException("查询TIDB的SQL语句未指定索引");
        }
    }
}
