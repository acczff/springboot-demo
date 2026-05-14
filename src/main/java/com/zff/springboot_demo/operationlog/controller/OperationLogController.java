package com.zff.springboot_demo.operationlog.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.operationlog.entity.OperationLog;
import com.zff.springboot_demo.operationlog.repository.OperationLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志查询接口。
 */
@RestController
@RequestMapping("/api/logs")
public class OperationLogController {

    private final OperationLogRepository operationLogRepository;

    public OperationLogController(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    /**
     * 分页查询操作日志，默认按创建时间倒序返回。
     */
    @GetMapping
    public Result<PageResult<OperationLog>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createTime")); // 最新的排前面
        Page<OperationLog> logs = operationLogRepository.findAll(pageable);
        PageResult<OperationLog> pageResult = new PageResult<>(logs.getContent(), logs.getTotalElements());
        return Result.success("查詢成功",pageResult);
    }
}
