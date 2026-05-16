package com.zff.springboot_demo.workorder.repository;

import com.zff.springboot_demo.workorder.entity.WorkOrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderItemRepository extends CrudRepository<WorkOrderItem,Long> {
}
