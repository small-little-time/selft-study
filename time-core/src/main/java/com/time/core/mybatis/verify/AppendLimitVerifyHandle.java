package com.time.core.mybatis.verify;

import com.time.core.mybatis.SQLStrategyForMybatis;
import com.time.core.mybatis.strategy.AppendLimitStrategy;
import com.time.core.mybatis.strategy.SQLStrategy;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/24 11:14
 */
public class AppendLimitVerifyHandle implements VerifyHandle {

    private static final Logger log = LoggerFactory.getLogger(AppendLimitVerifyHandle.class);

    private void log(String format, Object... arg) {
        if (!SQLStrategyForMybatis.Config.getSilence()) {
            log.error(format, arg);
        }
    }


    @Override
    public SQLStrategy verify(Invocation invocation, BoundSql boundSql) {

        String sql = boundSql.getSql().toLowerCase();
        String lowerCase = sql.toLowerCase().replaceAll("\t", " ").replaceAll("\n", " ").replaceAll("\r", " ");
        boolean noneLimit = !(lowerCase.substring(lowerCase.indexOf("from")).contains(" limit "));
        boolean notCount = !lowerCase.contains("count(");
        boolean needAppendLimit = noneLimit && notCount;

        if (needAppendLimit) {
            log("querySqlNoLimit sql={}", boundSql.getSql().replaceAll("\t", " ").replaceAll("\n", " "), new RuntimeException("查询SQL不包含分页Limit"));
        }
        if (SQLStrategyForMybatis.Config.getCheckOnly()) {
            return null;
        }

        return needAppendLimit ? new AppendLimitStrategy() : null;

    }


}
