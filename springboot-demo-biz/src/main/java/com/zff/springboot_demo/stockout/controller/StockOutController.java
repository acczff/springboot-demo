package com.zff.springboot_demo.stockout.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.stockout.dto.CreateStockOutRequest;
import com.zff.springboot_demo.stockout.entity.StockOutItem;
import com.zff.springboot_demo.stockout.entity.StockOutOrder;
import com.zff.springboot_demo.stockout.service.StockOutService;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-out-orders")
public class StockOutController {

    private final StockOutService stockOutService;
    private final UserService userService;

    public StockOutController(StockOutService stockOutService, UserService userService) {
        this.stockOutService = stockOutService;
        this.userService = userService;
    }

    private void requireAdmin(Long userId) {
        boolean isAdmin = userService.getUserRoles(userId).stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        if (!isAdmin) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作");
        }
    }

    /** ADMIN 创建出库单 */
    @PostMapping
    public Result<StockOutOrder> create(@RequestBody @Valid CreateStockOutRequest request,
                                        HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("currentUserId");
        requireAdmin(userId);
        return Result.success("创建成功", stockOutService.create(request, userId));
    }

    /** WORKER 拣货：PENDING → PICKING */
    @PutMapping("/{id}/pick")
    public Result<StockOutOrder> pick(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("拣货开始", stockOutService.pick(id, userId));
    }

    /** ADMIN 复核：PICKING → PENDING_REVIEW */
    @PutMapping("/{id}/review")
    public Result<StockOutOrder> review(@PathVariable Long id,
                                        @RequestParam(required = false) String reviewNote,
                                        HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("currentUserId");
        requireAdmin(userId);
        return Result.success("复核完成", stockOutService.review(id, userId, reviewNote));
    }

    /** ADMIN 确认出库：PENDING_REVIEW → COMPLETED，扣减库存 */
    @PutMapping("/{id}/complete")
    public Result<StockOutOrder> complete(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("currentUserId");
        requireAdmin(userId);
        return Result.success("出库完成，库存已扣减", stockOutService.complete(id, userId));
    }

    /** 查出库单列表，支持按 status 筛选 */
    @GetMapping
    public Result<List<StockOutOrder>> list(@RequestParam(required = false) String status) {
        if (status != null) {
            return Result.success("查询成功", stockOutService.findByStatus(status));
        }
        return Result.success("查询成功", stockOutService.findAll());
    }

    /** 查出库单详情 */
    @GetMapping("/{id}")
    public Result<StockOutOrder> detail(@PathVariable Long id) {
        return Result.success("查询成功", stockOutService.findById(id));
    }

    /** 查出库单明细 */
    @GetMapping("/{id}/items")
    public Result<List<StockOutItem>> items(@PathVariable Long id) {
        return Result.success("查询成功", stockOutService.findItems(id));
    }
}
