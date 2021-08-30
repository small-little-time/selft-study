package com.time.core.mybatis.verify;

import com.time.core.mybatis.strategy.SQLStrategy;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Invocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangYi zhangyi04@zhuanzhuan.com
 * @date 2021/8/24 20:14
 */
public class VerifyHandleContext {

    public List<VerifyHandle> verifyHandles = new ArrayList<>();

    public VerifyHandleContext() {
        verifyHandles.add(new AppendLimitVerifyHandle());
        verifyHandles.add(new ForceIndexVerifyHandle());
    }

    public List<SQLStrategy> process(Invocation invocation, BoundSql boundSql) {
        List<SQLStrategy> strategyList = new ArrayList<>();
        for (VerifyHandle verifyHandle : verifyHandles) {
            strategyList.add(verifyHandle.verify(invocation, boundSql));
        }
        return strategyList;
    }
}
