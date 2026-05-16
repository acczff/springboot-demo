package com.zff.springboot_demo.workreport.repository;

import com.zff.springboot_demo.workreport.entity.WorkReport;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface WorkReportRepository  extends JpaRepository<WorkReport, Long> {

}
