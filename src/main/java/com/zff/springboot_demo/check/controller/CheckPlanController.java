package com.zff.springboot_demo.check.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.check.dto.CreateCheckPlanRequest;
import com.zff.springboot_demo.check.dto.RecordActualRequest;
import com.zff.springboot_demo.check.dto.ReviewRequest;
import com.zff.springboot_demo.check.entity.CheckItem;
import com.zff.springboot_demo.check.entity.InventoryCheckPlan;
import com.zff.springboot_demo.check.service.CheckPlanService;
import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/check-plans")
public class CheckPlanController {

    private final CheckPlanService checkPlanService;
    private final UserService userService;

    public CheckPlanController(CheckPlanService checkPlanService, UserService userService) {
        this.checkPlanService = checkPlanService;
        this.userService = userService;
    }

    /** 主管创建盘点计划（传产品ID列表，系统快照账面数） */
    @PostMapping
    public Result<InventoryCheckPlan> create(@RequestBody @Valid CreateCheckPlanRequest request,
                                             HttpServletRequest httpRequest) {
        Long createdBy = (Long) httpRequest.getAttribute("currentUserId");
        boolean isAdmin = userService.getUserRoles(createdBy).stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        if (!isAdmin) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作");
        }
        return Result.success("创建成功",
                checkPlanService.create(request.getProductIds(), createdBy));
    }

    /** 主管开始盘点：PENDING → IN_PROGRESS */
    @PutMapping("/{id}/start")
    public Result<InventoryCheckPlan> start(@PathVariable Long id) {
        return Result.success("开始盘点", checkPlanService.start(id));
    }

    /** 盘点员录入实盘数 */
    @PutMapping("/{id}/items/{itemId}/record")
    public Result<CheckItem> record(@PathVariable Long id,
                                    @PathVariable Long itemId,
                                    @RequestBody @Valid RecordActualRequest request) {
        return Result.success("录入成功",
                checkPlanService.recordActual(id, itemId, request.getActualQuantity()));
    }

    /** 盘点员提交：IN_PROGRESS → PENDING_REVIEW，系统写入差异 */
    @PutMapping("/{id}/submit")
    public Result<InventoryCheckPlan> submit(@PathVariable Long id) {
        return Result.success("提交审核成功", checkPlanService.submit(id));
    }

    /** 主管审核通过：PENDING_REVIEW → COMPLETED，更新库存 */
    @PutMapping("/{id}/review")
    public Result<InventoryCheckPlan> review(@PathVariable Long id,
                                             @RequestBody ReviewRequest request,
                                             HttpServletRequest httpRequest) {
        Long reviewedBy = (Long) httpRequest.getAttribute("currentUserId");
        boolean isAdmin = userService.getUserRoles(reviewedBy).stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        if (!isAdmin) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作");
        }
        return Result.success("审核完成，库存已更新",
                checkPlanService.review(id, reviewedBy, request.getReviewNote()));
    }

    /** 查全部盘点计划 */
    @GetMapping
    public Result<List<InventoryCheckPlan>> list() {
        return Result.success("查询成功", checkPlanService.findAll());
    }

    /** 查盘点计划详情（含明细列表） */
    @GetMapping("/{id}")
    public Result<InventoryCheckPlan> getById(@PathVariable Long id) {
        return Result.success("查询成功", checkPlanService.findById(id));
    }

    /** 查某计划的明细列表 */
    @GetMapping("/{id}/items")
    public Result<List<CheckItem>> getItems(@PathVariable Long id) {
        return Result.success("查询成功", checkPlanService.findItems(id));
    }
}
