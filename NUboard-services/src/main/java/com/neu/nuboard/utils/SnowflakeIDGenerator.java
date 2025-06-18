package com.neu.nuboard.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.hutool.core.lang.Snowflake;

@Component
public class SnowflakeIDGenerator implements IDGenerator {
    private final Snowflake snowflake;
    private final long workerId;
    private final long datacenterId;

    public SnowflakeIDGenerator(
            @Value("${id.generator.workerId:1}") long workerId,
            @Value("${id.generator.datacenterId:1}") long datacenterId) {
        this.snowflake = new Snowflake(workerId, datacenterId);
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    @Override
    public long nextId() {
        return snowflake.nextId();
    }

    @Override
    public long getWorkerId() {
        return workerId;
    }

    @Override
    public long getDatacenterId() {
        return datacenterId;
    }
}