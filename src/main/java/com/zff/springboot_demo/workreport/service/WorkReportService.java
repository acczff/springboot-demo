package com.zff.springboot_demo.workreport.service;

import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.workreport.dto.WorkReportCreateRequest;
import com.zff.springboot_demo.workreport.entity.WorkReport;
import com.zff.springboot_demo.workreport.repository.WorkReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkReportService {

    private final WorkReportRepository workReportRepository;
    private final UserService  userService;

    public WorkReportService(WorkReportRepository workReportRepository, UserService userService) {
        this.workReportRepository = workReportRepository;
        this.userService = userService;
    }

    public WorkReport findById(Long id) {
        return workReportRepository.findById(id).orElseThrow(() -> new RuntimeException("报工记录不存在：" + id));
    }

    @Transactional
    public WorkReport create(WorkReportCreateRequest workReportCreateRequest,Long reportedBy) {
        WorkReport workReport = new WorkReport();
        User user = userService.findById(reportedBy);
        workReport.setWorkOrderId(workReportCreateRequest.getWorkOrderId());
        workReport.setReportedQty(workReportCreateRequest.getReportedQty());
        workReport.setReportedBy(reportedBy);
        workReport.setReportedByName(user.getUsername());
        workReport.setReportedTime(System.currentTimeMillis());
        workReport.setStatus("DRAFT");
        return workReportRepository.save(workReport);
    }
}
