package com.zff.springboot_demo.operationlog.service;

import com.zff.springboot_demo.operationlog.entity.OperationLog;
import com.zff.springboot_demo.operationlog.repository.OperationLogRepository;
import org.springframework.stereotype.Service;

/**
 * 操作日志业务逻辑层，负责保存日志记录。
 */
@Service
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    public OperationLogService(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    /**
     * 保存一条操作日志。
     */
    public void save(OperationLog log) {
        operationLogRepository.save(log);
    }
}
