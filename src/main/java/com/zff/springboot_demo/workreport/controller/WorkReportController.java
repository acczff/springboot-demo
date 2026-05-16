package com.zff.springboot_demo.workreport.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.workreport.dto.WorkReportCreateRequest;
import com.zff.springboot_demo.workreport.entity.WorkReport;
import com.zff.springboot_demo.workreport.service.WorkReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/work-reports")
public class WorkReportController {

    private final WorkReportService workReportService;

    public WorkReportController(WorkReportService workReportService) {
        this.workReportService = workReportService;
    }

    @GetMapping("/{id}")
    public Result<WorkReport> findById(@PathVariable Long id) {
        return Result.success("查询成功", workReportService.findById(id));
    }

    @PostMapping
    public Result<WorkReport> create(
            @RequestBody @Valid WorkReportCreateRequest request,
            HttpServletRequest httpRequest) {
        Long reportedBy = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("创建成功", workReportService.create(request, reportedBy));
    }
}
