package com.neu.nuboard.utils;

public interface IDGenerator {
    //配置
    long getWorkerId();
    long getDatacenterId();
    long getEpoch(); 
    long getWorkerIdBits();     
    long getDatacenterIdBits();  
    long getSequenceBits();   

    //运行时状态方法 生成id 序列号 上次生成id时间戳 获取配置信息
    long nextId();
    long getSequence(); 
    long getLastTimestamp();
    String getConfigInfo();
    
}
