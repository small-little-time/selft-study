package com.time.core.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



@Configuration
@ConfigurationProperties(prefix = SqlStrategyConfig.SQL_STRATEGY_PREFIX)
public class SqlStrategyConfig {

    public static final String SQL_STRATEGY_PREFIX = "sqlstrategy";

    private String enable;

    private String maxLimit;

    private String checkOnly;

    private String silence;

    private String checkUrlList;

    public String getCheckUrlList() {
        return checkUrlList;
    }

    public void setCheckUrlList(String checkUrlList) {
        this.checkUrlList = checkUrlList;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getCheckOnly() {
        return checkOnly;
    }

    public void setCheckOnly(String checkOnly) {
        this.checkOnly = checkOnly;
    }

    public String getSilence() {
        return silence;
    }

    public void setSilence(String silence) {
        this.silence = silence;
    }


}
