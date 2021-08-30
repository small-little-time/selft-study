package com.time.core.mybatis.verify;

import com.time.core.mybatis.strategy.SQLStrategy;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Invocation;

/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/24 11:12
 */
public interface VerifyHandle {

    /**
     * 验证是否需要执行某个策略
     *
     * @param invocation
     * @param boundSql
     * @return
     */
    SQLStrategy verify(Invocation invocation, BoundSql boundSql);

}
