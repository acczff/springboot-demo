package com.zff.springboot_demo.workorder.service;

import com.zff.springboot_demo.workorder.entity.ProductionRecord;
import com.zff.springboot_demo.workorder.repository.ProductionRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductionRecordService {

    private final ProductionRecordRepository productionRecordRepository;

    public ProductionRecordService(ProductionRecordRepository productionRecordRepository) {
        this.productionRecordRepository = productionRecordRepository;
    }

    public ProductionRecord save(ProductionRecord productionRecord) {
        return productionRecordRepository.save(productionRecord);
    }


}
