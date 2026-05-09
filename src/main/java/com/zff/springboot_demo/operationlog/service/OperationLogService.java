package com.zff.springboot_demo.operationlog.service;

import com.zff.springboot_demo.operationlog.entity.OperationLog;
import com.zff.springboot_demo.operationlog.repository.OperationLogRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    public OperationLogService(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    public void save(OperationLog log) {
        operationLogRepository.save(log);
    }
}
