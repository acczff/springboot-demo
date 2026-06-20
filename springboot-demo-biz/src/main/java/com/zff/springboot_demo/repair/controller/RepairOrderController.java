package com.zff.springboot_demo.repair.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.repair.dto.CompleteRepairRequest;
import com.zff.springboot_demo.repair.dto.ConfirmRepairRequest;
import com.zff.springboot_demo.repair.dto.CreateRepairRequest;
import com.zff.springboot_demo.repair.entity.RepairOrder;
import com.zff.springboot_demo.repair.service.RepairOrderService;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repair-orders")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;
    private final UserService userService;

    public RepairOrderController(RepairOrderService repairOrderService, UserService userService) {
        this.repairOrderService = repairOrderService;
        this.userService = userService;
    }

    /** 查详情 */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success("查询成功", repairOrderService.findById(id));
    }

    /** 列表分页（数据级权限：ADMIN看全部，维修员只看自己的） */
    @GetMapping
    public Result list(
            @RequestParam(required = false) String status,
            Pageable pageable,
            HttpServletRequest httpRequest) {
        Long currentUserId = (Long) httpRequest.getAttribute("currentUserId");
        boolean isAdmin = userService.getUserRoles(currentUserId)
                .stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        Page<RepairOrder> page = repairOrderService.findByStatus(status, pageable, currentUserId, isAdmin);
        return Result.success("查询成功", new PageResult<>(page.getContent(), page.getTotalElements()));
    }

    /** 操作工报修 */
    @PostMapping
    public Result create(@RequestBody @Valid CreateRepairRequest request,
                         HttpServletRequest httpRequest) {
        Long reporterId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("报修成功", repairOrderService.create(request.getEquipmentCode(), request.getFaultDescription(), reporterId));
    }

    /** 主管派单：PENDING → ASSIGNED */
    @PutMapping("/{id}/assign")
    public Result assign(@PathVariable Long id,
                         @RequestParam Long repairerId,
                         HttpServletRequest httpRequest) {
        Long supervisorId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("派单成功", repairOrderService.assign(id, supervisorId, repairerId));
    }

    /** 维修员接单/开始维修：ASSIGNED → IN_PROGRESS */
    @PutMapping("/{id}/start")
    public Result start(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long repairerId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("开始维修", repairOrderService.start(id, repairerId));
    }

    /** 维修员完成维修：IN_PROGRESS → PENDING_CONFIRM */
    @PutMapping("/{id}/complete")
    public Result complete(@PathVariable Long id,
                           @RequestBody @Valid CompleteRepairRequest request,
                           HttpServletRequest httpRequest) {
        Long repairerId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("维修完成", repairOrderService.complete(id, repairerId, request.getRepairResult()));
    }

    /** 操作工确认：PENDING_CONFIRM → CLOSED */
    @PutMapping("/{id}/confirm")
    public Result confirm(@PathVariable Long id,
                          @RequestBody @Valid ConfirmRepairRequest request,
                          HttpServletRequest httpRequest) {
        Long reporterId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("确认完成", repairOrderService.confirm(id, reporterId, request.getConfirmationOpinion()));
    }
}
