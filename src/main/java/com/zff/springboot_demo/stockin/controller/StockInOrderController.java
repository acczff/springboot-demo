package com.zff.springboot_demo.stockin.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.stockin.dto.CreateStockInRequest;
import com.zff.springboot_demo.stockin.dto.ShelveRequest;
import com.zff.springboot_demo.stockin.entity.StockInOrder;
import com.zff.springboot_demo.stockin.service.StockInOrderService;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-in-orders")
public class StockInOrderController {

    private final StockInOrderService stockInOrderService;
    private final UserService userService;

    public StockInOrderController(StockInOrderService stockInOrderService, UserService userService) {
        this.stockInOrderService = stockInOrderService;
        this.userService = userService;
    }

    /** 查详情 */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success("查询成功", stockInOrderService.findById(id));
    }

    /** 列表分页（ADMIN看全部，库管员只看自己创建的） */
    @GetMapping
    public Result list(@RequestParam(required = false) String status,
                       Pageable pageable, HttpServletRequest httpRequest) {
        Long currentUserId = (Long) httpRequest.getAttribute("currentUserId");
        boolean isAdmin = userService.getUserRoles(currentUserId).stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        Page<StockInOrder> page = stockInOrderService.findByStatus(status, pageable, currentUserId, isAdmin);
        return Result.success("查询成功", new PageResult<>(page.getContent(), page.getTotalElements()));
    }

    /** 创建入库单 */
    @PostMapping
    public Result create(@RequestBody @Valid CreateStockInRequest request,
                         HttpServletRequest httpRequest) {
        Long createdBy = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("创建成功",
                stockInOrderService.create(request.getProductId(), request.getQuantity(), createdBy));
    }

    /** 验收：PENDING_RECEIVE → RECEIVED */
    @PutMapping("/{id}/receive")
    public Result receive(@PathVariable Long id) {
        return Result.success("验收成功", stockInOrderService.receive(id));
    }

    /** 上架：RECEIVED → SHELVED（事务：状态变更 + 库存累加） */
    @PutMapping("/{id}/shelve")
    public Result shelve(@PathVariable Long id, @RequestBody @Valid ShelveRequest request) {
        return Result.success("上架成功", stockInOrderService.shelve(id, request.getLocation()));
    }
}
