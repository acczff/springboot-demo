package com.zff.springboot_demo.workorder.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.workorder.dto.ProductRequest;
import com.zff.springboot_demo.workorder.entity.Product;
import com.zff.springboot_demo.workorder.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<PageResult<Product>> findAll(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Product> page = productService.findAll(keyword, pageable);
        return Result.success("查询成功", new PageResult<>(page.getContent(), page.getTotalElements()));
    }

    @GetMapping("/{id}")
    public Result<Product> findById(@PathVariable Long id) {
        return Result.success("查询成功", productService.findById(id));
    }

    @GetMapping("/code/{code}")
    public Result<Product> findByCode(@PathVariable String code) {
        return Result.success("查询成功", productService.findByCode(code));
    }

    @PostMapping
    public Result<Product> create(@RequestBody @Valid ProductRequest request) {
        return Result.success("创建成功", productService.create(request));
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        return Result.success("更新成功", productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success("删除成功");
    }
}
