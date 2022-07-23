package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
