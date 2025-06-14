package com.neu.nuboard.utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class SnowflakeIDGenerator implements IDGenerator {
    @Value("${id.generator.workerId}")
    private final long workerId;
    private final long datacenterId;
    private final long epoch;
    private final long workerIdBits;
    private final long datacenterIdBits;
    private final long sequenceBits;
    
    // 运行时状态
    private long sequence = 0L;
    private long lastTimestamp = -1L;   

    // 位运算常量
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // 位移常量
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    
   
}