package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ReportRepository extends JpaRepository<Report, Long> {

    void deleteAllByBadfolderIdIn(List<Long> dbLongList);

    void deleteByReporterId(Long id);

    Optional<Report> findByBadfolderIdAndReporterId(Long badfolderid, Long reporterid);
}
