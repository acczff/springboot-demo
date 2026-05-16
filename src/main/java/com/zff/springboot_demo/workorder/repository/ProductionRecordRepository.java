package com.zff.springboot_demo.workorder.repository;

import com.zff.springboot_demo.workorder.entity.ProductionRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRecordRepository extends CrudRepository<ProductionRecord,Long> {
}
