package com.time.core.mybatis.strategy;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Invocation;

/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/23 17:19
 */
public interface SQLStrategy {

    /**
     * 具体sql策略执行
     *
     * @param invocation
     * @param boundSql
     */
    void strategyExecute(Invocation invocation, BoundSql boundSql);

}
