package com.zff.springboot_demo.inspection.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.inspection.dto.InspectionOrderCreateRequest;
import com.zff.springboot_demo.inspection.dto.InspectionOrderFailRequest;
import com.zff.springboot_demo.inspection.dto.InspectionOrderReviewRequest;
import com.zff.springboot_demo.inspection.entity.InspectionOrder;
import com.zff.springboot_demo.inspection.service.InspectionOrderService;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inspection-orders")
public class InspectionOrderController {

    private final InspectionOrderService inspectionOrderService;
    private final UserService userService;

    public InspectionOrderController(InspectionOrderService inspectionOrderService, UserService userService) {
        this.inspectionOrderService = inspectionOrderService;
        this.userService = userService;
    }

    /** 查询质检单详情 */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success("查询成功", inspectionOrderService.findById(id));
    }

    /** 查询质检单列表（按工单查 或 按状态分页查）
     *  数据级权限：ADMIN 看全部；其他角色只看自己（inspectorId = 当前用户）
     */
    @GetMapping
    public Result list(
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(required = false) String status,
            Pageable pageable,
            HttpServletRequest httpRequest) {
        if (workOrderId != null) {
            return Result.success("查询成功", inspectionOrderService.findByWorkOrderId(workOrderId));
        }
        Long currentUserId = (Long) httpRequest.getAttribute("currentUserId");
        boolean isAdmin = userService.getUserRoles(currentUserId)
                .stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        Page<InspectionOrder> page = inspectionOrderService.findByStatus(status, pageable, currentUserId, isAdmin);
        return Result.success("查询成功", new PageResult<>(page.getContent(), page.getTotalElements()));
    }

    /**
     * 创建质检单。
     * 创建人从 token 自动注入，不允许前端传。
     */
    @PostMapping
    public Result create(@RequestBody @Valid InspectionOrderCreateRequest request,
                         HttpServletRequest httpRequest) {
        Long createdBy = (Long) httpRequest.getAttribute("currentUserId");
        InspectionOrder order = inspectionOrderService.create(request, createdBy);
        return Result.success("创建成功", order);
    }

    /**
     * 开始检验：PENDING → INSPECTING。
     * 检验人为当前登录用户。
     */
    @PutMapping("/{id}/start")
    public Result start(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long inspectorId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("开始检验", inspectionOrderService.start(id, inspectorId));
    }

    /** 检验通过：INSPECTING → PASS */
    @PutMapping("/{id}/pass")
    public Result pass(@PathVariable Long id) {
        return Result.success("检验通过", inspectionOrderService.pass(id));
    }

    /** 检验不合格：INSPECTING → FAIL（必须填写不合格原因） */
    @PutMapping("/{id}/fail")
    public Result fail(@PathVariable Long id,
                       @RequestBody @Valid InspectionOrderFailRequest request) {
        return Result.success("检验不合格", inspectionOrderService.fail(id, request));
    }

    /** 发起评审：FAIL → REVIEWING */
    @PutMapping("/{id}/start-review")
    public Result startReview(@PathVariable Long id) {
        return Result.success("已发起评审", inspectionOrderService.startReview(id));
    }

    /** 完成评审：REVIEWING → REVIEWED（必须填写评审意见和处置方式） */
    @PutMapping("/{id}/review")
    public Result review(@PathVariable Long id,
                         @RequestBody @Valid InspectionOrderReviewRequest request,
                         HttpServletRequest httpRequest) {
        Long reviewerId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("评审完成", inspectionOrderService.review(id, request, reviewerId));
    }

    /** 执行处置：REVIEWED → DISPOSED（REWORK 时自动创建新质检单） */
    @PutMapping("/{id}/dispose")
    public Result dispose(@PathVariable Long id) {
        return Result.success("处置完成", inspectionOrderService.dispose(id));
    }
}
