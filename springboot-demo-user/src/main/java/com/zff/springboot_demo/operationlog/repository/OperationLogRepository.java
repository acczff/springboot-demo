package com.zff.springboot_demo.operationlog.repository;

import com.zff.springboot_demo.operationlog.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 操作日志数据访问层，提供日志表基础 CRUD 操作。
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog,Long> {
}
